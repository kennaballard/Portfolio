<?php
    $this_dir = __DIR__;
    // ==============================================================
    // format_time_info()
    // inputs: integer (unix_ts)
    //         local time offset in seconds
    // outputs: Formatted time
    // ==============================================================
    function format_time_info($input, $offset = 0) {
        
        // use time offset and unix timestamp to get client's time
        $client_time = date('m/d/Y g:iA',($input-($offset*60)));    
        
        return $client_time;
    }
?>