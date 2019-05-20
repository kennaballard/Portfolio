#!/bin/bash
set -euo pipefail

# CONSTANTS
declare -rx PROCESS_FILE="/var/asg4/processes.csv"
declare -rx DELIMETER=','
declare -Arx FIELD_NAMES=([pid]=0 [tid]=1 [pname]=2 [priority]=3 [remainingTime]=4 [availTime]=5 [burstTime]=6 [agedPriority]=7)
declare -rx IO_BLOCKTIME=8
declare -rx QUANTUM=200
declare -rx HW_CS_TIME=10
declare -rx LW_CS_TIME=2
declare -rix NUM_PROCESSES=`wc -l $PROCESS_FILE | egrep -o '^[^ ]* '`
declare -arx PROCESSES_INDEXES=(`seq 0 $(( NUM_PROCESSES - 1))`)
declare -arx EXIT_STATES=('Terminated' 'Quantum Expired' 'Blocking I/O')

declare -a processes

# READ IN THE PROCESSES
function readFile(){
	#processes=`cat $PROCESS_FILE` 
	mapfile -t processes < $PROCESS_FILE
	initializeAgedPriorities	
}

function getField(){
	local processIdx=$1
	local fieldName=$2
	local fieldIdx=${FIELD_NAMES[$fieldName]}

	local process=${processes[$processIdx]}
	echo $process | cut -d $DELIMETER -f $(( fieldIdx+1))
}

# SET VALUE OF A SPECIFIC FIELD IN A PROCESS
function setField(){
	local processIdx=$1
	local fieldName=$2
	local fieldIdx=${FIELD_NAMES[$fieldName]}
	local value=$3
	local str=${processes[$processIdx]}
	local strLength=${#str}
	local delimLen=${#DELIMETER}
		
	# SAVE PROCESS INTO TEMP ARRAY
	local delimCount=0
	local fields=()
	local wordLen=0
	local strPos=0
	local i=0
	
	# GO THROUGH ENTIRE STRING --> SPLIT INTO THREE SECTIONS
	while [ $i -lt $strLength ] ; do
		if [ $DELIMETER == ${str:$i:$delimLen} ]; then
			delimCount=$(( delimCount + 1 ))

			# SEPARATE VALUE TO BE CHANGED FROM THE REST
			if [ $delimCount -eq $(( fieldIdx )) ] || [ $delimCount -eq $(( fieldIdx + 1 )) ]; then
				fields+=(${str:$strPos:$wordLen})
				strPos=$(( i + delimLen ))
				wordLen=0
				i=$(( i + delimLen ))
			fi
		fi
		i=$(( i + 1 ))
		wordLen=$(( wordLen + 1))
	done

	fields+=(${str:strPos:$wordLen})

	# CHANGE SPECIFIED VALUE AND RETURN TO PROCESSES
	# NEEDS ADJUSTMENT IF CHANGE IS ON FIRST OR LAST FIELD
	# 	--> 'FIELDS' WILL ONLY HAVE 2 SECTIONS
	case $fieldIdx in
		0) fields[0]=$value
		   saved="${fields[0]},${fields[1]}";;
		7) fields[1]=$value
		   saved="${fields[0]},${fields[1]}";;
		*) fields[1]=$value
		   saved="${fields[0]},${fields[1]},${fields[2]}";;
	esac
	processes[$processIdx]=$saved
}


# ADD AN AGED PRIORITY FIELD TO ALL PROCESSES
function initializeAgedPriorities(){
	for((i=0; i<$NUM_PROCESSES; i++))
	do
		processes[$i]="${processes[$i]},`getField $i 'priority'`"
	done
}

# CALCULATE THE TOTAL REMAINING RUNTIME
function getRemainingTime(){
	local total=0
	for ((i=0; i<$NUM_PROCESSES; i++))
	do
		total=$(( total + `getField $i 'remainingTime'` ))
	done

	echo $total
}

# DETERMINE WHICH PROCESSES ARE READY
function getEligibleProcesses(){
	local currentTime=$1

	local eligible=""
	for ((i=0; i<$NUM_PROCESSES; i++))
	do
		remaining=`getField $i 'remainingTime'`
		avail=`getField $i 'availTime'`
		if [ $remaining -gt 0 ] && [ $avail -le $currentTime ]; then
			eligible+="$i "
		fi
	done
	echo "${eligible[@]}"
}

# GET THE NEXT PROCESS TO RUN FROM READY PROCESSES
# --> HIGHEST PRIORITY
# --> SJF TIEBREAKER
# RETURNS INDEX POSITION IN PROCESSES ARRAY
function getNextProcess(){
	local currentTime=$1	
	local prevProc=$2
	currentProcess=$3

	# GET ELIGIBLE PROCESSES
	local eligibleArr=(`getEligibleProcesses $currentTime`)
	if [[ ${#eligibleArr[@]} -gt 0 ]] ; then
		local highPriorIdx=${eligibleArr[0]}
		local highPrior=`getField ${eligibleArr[0]} 'agedPriority'`
	fi	
	#echo "ELIGIBLE:"
	for ((i=1; i<${#eligibleArr[@]}; i++))
	do
		
		if [[ ${eligibleArr[$i]} -eq $prevProc ]] && [[ ${#eligibleArr[@]} -gt 1 ]] ; then
			continue
		fi
		#echo ${processes[${eligibleArr[$i]}]}
		currPrior=`getField ${eligibleArr[$i]} 'agedPriority'`
		# COMPARE PRIORITIES
		if [[ $currPrior -gt $highPrior ]]; then
			highPrior=$currPrior
			highPriorIdx=${eligibleArr[$i]}
		# ON TIE, COMPARE BURST TIME
		elif [[ $currPrior -eq $highPrior ]]; then
			currBurst=`getField ${eligibleArr[$i]} 'burstTime'`
			highBurst=`getField $highPriorIdx 'burstTime'`
			if [[ $currBurst -lt $highBurst ]]; then
				highPrior=$currPrior
				highPriorIdx=${eligibleArr[$i]}
			fi
		fi
	done
	#echo "---------------------------------------"

	if [[ ${#eligibleArr[@]} -eq 0 ]]; then
		# RETURN -1 IF THERE ARE NO ELIGIBLE PROCESSES
		eval "currentProcess='-1'"
	else
		# AGE OTHER ELIGIBLE PROCESSES
		ageProcess $highPriorIdx "${eligibleArr[@]}"
		# RETURN INDEX POSITION IN PROCESSES ARRAY OF NEXT PROCESS
		eval "currentProcess='$highPriorIdx'"
	fi
}

# RUN THE NEXT PROCESS
# --> MODIFY REMAINING TIME IN PROCESS & RESET PRIORITY
function runProcess(){
	local runTime=$1
	local nextProcessIdx=$2
	local remaining=`getField $nextProcessIdx 'remainingTime'`

	if [[ $remaining -eq $runTime ]]; then
		echo 0
	elif [[ $QUANTUM -eq $runTime ]]; then
		echo 1
	else
		echo 2
	fi
}


# RETURN THE RUNTIME OF PROCESS THAT HAS RUN MOST RECENTLY
function getRuntime(){
	local currentProc=$1

	local burst=`getField $currentProc 'burstTime'`
	local remain=`getField $currentProc 'remainingTime'`
	
	# FIND MINIMUM RUNTIME
	if [[ $burst -lt $QUANTUM ]]; then
		if [[ $burst -lt $remain ]]; then
			echo $burst
		elif [[ $remain -lt $QUANTUM ]]; then
			echo $remain
		fi
	elif [[ $remain -lt $QUANTUM ]]; then
		echo $remain
	else
		echo $QUANTUM
	fi 
}

# AGE ALL ELIGIBLE PROCESSES THAT ARE NOT NEXT PROCESS
function ageProcess(){
	local chosenProc=$1; shift
	local eligibleArr=($@)

	for ((i=0; i<${#eligibleArr[@]}; i++))
	do
		# DO NOT AGE CHOSEN PROCESS
		if [ ${eligibleArr[$i]} -ne $chosenProc ]; then
			priority="`getField ${eligibleArr[$i]} 'agedPriority'`"
			priority=$(( priority + 1 ))
			setField "${eligibleArr[$i]}" 'agedPriority' "$priority"
		fi
	done
}

function displayIdle(){
	local start=$1
	local end=$2
	local remains=$3

	printf "%5s..%5s: ********** IDLE **********, Total time remaining (all threads): %1s.\n" "$start" "$end" "$remains"
}

function displayCS(){
	local start=$1
	local end=$2
	local cs=$3

	printf "%5s..%5s: %2s context switch\n" "$start" "$end" "$cs"
}

function displayProc(){
	local start=$1
	local end=$2
	local proc=$3
	local status=$4
	local remains=$5
	
	printf "%5s..%5s: pid[%2s], tid[%3s], priority[%2s], remaining time[%4s], burst length [%3s].  Status: %15s.  Total time remaining (all threads): %1s.\n" $start $end "`getField $proc 'pid'`" "`getField $proc 'tid'`" "`getField $proc 'agedPriority'`" "`getField $proc 'remainingTime'`" "`getField $proc 'burstTime'`" "$status" "$remains"
}

# DISPLAY SCHEDULER RESULTS
function displayStats(){
	local totalTime=$1
	local idleTime=$2
	local lightCS=$3
	local heavyCS=$4

	local inUse=$(( totalTime - idleTime ))
	local util=`echo "scale=4; 100-(($idleTime / $inUse) * 100)" | bc -l`
	local LCStime=$(( lightCS * LW_CS_TIME ))
	local HCStime=$(( heavyCS * HW_CS_TIME ))

	local procTime=`echo "($totalTime - $idleTime - $LCStime - $HCStime) / $totalTime * 100" | bc -l`
	# STAT CALCULATIONS
	printf "%s" "-----------------------------------------------------------------------------------------------------------------------------------------------------------------"

	printf "\n%-60s:%6.2f%%" "CPU Utilization (100 - (idle / in use))" "$util"
	printf "\n%-60s:%7s" "# of lightweight context switches" "$lightCS" 
	printf "\n%-60s:%7s" "# of heavyweight context switches" "$heavyCS"
	printf "\n%-60s:%7s" "Lightweight context switch time" "$LCStime"
	printf "\n%-60s:%7s" "Heavyweight context switch time" "$HCStime"
	printf "\n%-60s:%6.2f%%\n" "% CPU time spent processing (i.e. not CS'ing, not idle)" "$procTime"
}

function updateValues(){
	local runTime=$1
	local nextProcessIdx=$2
	# RESET PRIORITY
	local prior="`getField $nextProcessIdx 'priority'`"
	setField "$nextProcessIdx" 'agedPriority' "$prior"
	
	# MODIFY REMAINING TIME
	local remaining="`getField $nextProcessIdx 'remainingTime'`"
	setField "$nextProcessIdx" 'remainingTime' $(( remaining - runTime ))
}

# LOAD PROCESSES
readFile

# DECLARE COUNTERS
currentTime=0
idleTime=0
idleState=0

lightCS=0
heavyCS=0

currentProcess=-1
prevProc=-1

while [[ "`getRemainingTime`" -gt 0 ]]
do
	#echo '------ CURRENT TIME ------' $currentTime
	prevProc=$currentProcess
	getNextProcess $currentTime $prevProc currentProcess

	############################### IF PROCESS IS IDLE ###########################
	if [[ $currentProcess -eq -1 ]]; then
		# Is idle
		idleState=0
		while [ $currentProcess -eq -1 ]
		do
			idleTime=$(( idleTime + 1 ))
			idleState=$(( idleState + 1 ))
			getNextProcess $(( currentTime + idleState )) $prevProc currentProcess
		done
		#### DISPLAY: idle info ####
		displayIdle $currentTime $(( currentTime + idleState-1 )) "`getRemainingTime`"
		currentTime=$(( currentTime + idleState ))
	fi
	
	########################## DETERMINE CONTEXT SWITCH TYPE #####################
	if [[ "`getField $currentProcess 'pid'`" -eq "`getField $prevProc 'pid'`" ]]; then
		# Light weight context switch
		if [[ "`getField $currentProcess 'tid'`" -ne "`getField $prevProc 'tid'`" ]]; then
			# Opposite would only occur after idle --> same thread, no context switch
			lightCS=$(( lightCS + 1 ))
			#### DISPLAY: context switch info ####
			displayCS $currentTime $(( currentTime + LW_CS_TIME -1 )) 'LW'
			currentTime=$(( currentTime + LW_CS_TIME ))
		fi
	else
		# Heavy weight context switch
		heavyCS=$(( heavyCS + 1 ))
		#### DISPLAY: context switch info ####
		displayCS $currentTime $(( currentTime + HW_CS_TIME - 1 )) 'HW'
		currentTime=$(( currentTime + HW_CS_TIME ))
	fi
		
	############################# RUN CURRENT PROCESS ############################
	runTime=`getRuntime $currentProcess`
	state="`runProcess $runTime $currentProcess`"

	###### DISPLAY: process info ####
	displayProc $currentTime $(( currentTime + runTime - 1 )) $currentProcess "${EXIT_STATES[$state]}" $(( `getRemainingTime` - runTime ))

	
	############################### UPDATE INFO ##################################
	currentTime=$(( currentTime + runTime ))
	updateValues "$runTime" "$currentProcess"
	# Add available delay if blocking
	if [[ $state -eq 2 ]]; then
		setField $currentProcess 'availTime' $(( currentTime + IO_BLOCKTIME ))
	fi
done

# Display final stats
displayStats $currentTime $idleTime $lightCS $heavyCS
