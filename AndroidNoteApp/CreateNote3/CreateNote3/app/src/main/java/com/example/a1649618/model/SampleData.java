package com.example.a1649618.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.a1649618.createnote.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class SampleData {

    private static final Date CREATED_START;
    private static final Date REMINDER_START;
    private static final long MONTH_DURATION = 31L * 24L * 60L * 60L * 1000L;

    static {
        long current = new Date().getTime();
        CREATED_START = new Date(current - MONTH_DURATION);
        REMINDER_START = new Date(current);
    }

    private static final double PERCENT_WITH_REMINDER = 0.5;

    private static final long SEED = 1L;

    private static final String TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Pretium aenean pharetra magna ac. Amet risus nullam eget felis eget nunc lobortis. Congue quisque egestas diam in. Praesent semper feugiat nibh sed. Sem nulla pharetra diam sit amet nisl. Sapien et ligula ullamcorper malesuada proin libero nunc consequat. Platea dictumst quisque sagittis purus sit amet. Lectus quam id leo in vitae turpis massa sed elementum. Amet massa vitae tortor condimentum lacinia quis. Euismod elementum nisi quis eleifend quam adipiscing vitae proin sagittis. Id semper risus in hendrerit gravida rutrum. Curabitur gravida arcu ac tortor. Ipsum dolor sit amet consectetur adipiscing elit ut aliquam. Venenatis lectus magna fringilla urna porttitor rhoncus dolor purus non. Turpis egestas integer eget aliquet nibh praesent. Nulla at volutpat diam ut venenatis. Ultrices tincidunt arcu non sodales neque sodales ut. Ultrices tincidunt arcu non sodales neque sodales ut.\n" +
            "Eget aliquet nibh praesent tristique magna sit. Dui ut ornare lectus sit. Augue ut lectus arcu bibendum. Mi tempus imperdiet nulla malesuada pellentesque elit eget gravida cum. Ornare quam viverra orci sagittis eu volutpat odio facilisis mauris. Ullamcorper velit sed ullamcorper morbi tincidunt ornare massa eget egestas. Pulvinar sapien et ligula ullamcorper malesuada proin libero nunc. Leo urna molestie at elementum eu facilisis. Consequat id porta nibh venenatis cras sed felis eget. In tellus integer feugiat scelerisque. In dictum non consectetur a erat. Nulla facilisi etiam dignissim diam quis enim. Mi in nulla posuere sollicitudin aliquam ultrices sagittis.\n" +
            "Dolor sit amet consectetur adipiscing elit pellentesque habitant morbi tristique. Sem nulla pharetra diam sit amet nisl. Amet commodo nulla facilisi nullam vehicula ipsum a arcu cursus. Orci dapibus ultrices in iaculis nunc sed augue lacus. Mi proin sed libero enim sed faucibus turpis in. Ut porttitor leo a diam sollicitudin tempor id. Adipiscing bibendum est ultricies integer quis auctor elit sed. Purus in massa tempor nec feugiat nisl. Tempus imperdiet nulla malesuada pellentesque elit eget. Nibh nisl condimentum id venenatis a condimentum vitae sapien pellentesque. Massa massa ultricies mi quis hendrerit dolor magna eget. Magna fringilla urna porttitor rhoncus dolor. Elit eget gravida cum sociis natoque penatibus et. Vitae purus faucibus ornare suspendisse sed nisi lacus sed. Tempor commodo ullamcorper a lacus vestibulum sed. Cras adipiscing enim eu turpis egestas pretium. Quis varius quam quisque id diam vel quam elementum pulvinar. Pellentesque elit ullamcorper dignissim cras tincidunt lobortis feugiat vivamus.\n" +
            "Ultrices mi tempus imperdiet nulla malesuada. Nibh ipsum consequat nisl vel pretium lectus quam. Vitae semper quis lectus nulla. Neque convallis a cras semper auctor neque vitae. Sit amet nisl suscipit adipiscing bibendum est ultricies. Condimentum lacinia quis vel eros donec ac odio tempor orci. Eros in cursus turpis massa tincidunt dui ut ornare lectus. Sit amet cursus sit amet dictum. Nisl condimentum id venenatis a condimentum vitae sapien pellentesque. Maecenas pharetra convallis posuere morbi leo urna molestie at elementum. Dignissim suspendisse in est ante in nibh mauris cursus mattis. Velit sed ullamcorper morbi tincidunt. Vivamus at augue eget arcu dictum varius duis at consectetur. Proin sed libero enim sed faucibus turpis in. Quis risus sed vulputate odio ut. Mi sit amet mauris commodo. Id neque aliquam vestibulum morbi blandit. Metus dictum at tempor commodo ullamcorper.\n" +
            "Amet consectetur adipiscing elit pellentesque. Sit amet risus nullam eget felis eget. Elit ullamcorper dignissim cras tincidunt lobortis feugiat vivamus. Scelerisque fermentum dui faucibus in ornare quam viverra orci. Lectus mauris ultrices eros in cursus turpis massa tincidunt dui. Mauris vitae ultricies leo integer malesuada nunc vel risus. Scelerisque purus semper eget duis. Vitae purus faucibus ornare suspendisse. Purus viverra accumsan in nisl nisi scelerisque eu ultrices. Ut aliquam purus sit amet luctus. Lobortis mattis aliquam faucibus purus in massa tempor. Eu augue ut lectus arcu bibendum at varius. Consectetur adipiscing elit pellentesque habitant morbi tristique senectus et netus. Praesent elementum facilisis leo vel fringilla. Bibendum ut tristique et egestas quis. Duis ultricies lacus sed turpis tincidunt id aliquet risus feugiat.\n" +
            "Amet consectetur adipiscing elit duis tristique sollicitudin. Tristique et egestas quis ipsum suspendisse ultrices gravida dictum. Aliquam sem et tortor consequat id. Justo eget magna fermentum iaculis. Posuere ac ut consequat semper viverra nam libero. Netus et malesuada fames ac turpis egestas sed tempus. Maecenas pharetra convallis posuere morbi leo urna molestie. Mi ipsum faucibus vitae aliquet nec ullamcorper. Nibh sit amet commodo nulla facilisi nullam vehicula ipsum a. Fermentum et sollicitudin ac orci phasellus egestas. Augue mauris augue neque gravida in fermentum et. Nisl vel pretium lectus quam id. Accumsan in nisl nisi scelerisque. Eu augue ut lectus arcu bibendum at varius.\n" +
            "Sed velit dignissim sodales ut eu. Vulputate sapien nec sagittis aliquam malesuada bibendum. Nisi porta lorem mollis aliquam ut porttitor leo. Ligula ullamcorper malesuada proin libero nunc consequat interdum varius sit. Nisi porta lorem mollis aliquam. Molestie nunc non blandit massa enim nec dui nunc. Iaculis at erat pellentesque adipiscing commodo elit at. Vestibulum mattis ullamcorper velit sed ullamcorper. Magna etiam tempor orci eu lobortis elementum nibh tellus. Quis imperdiet massa tincidunt nunc pulvinar. Porttitor eget dolor morbi non arcu risus quis varius quam. Lectus urna duis convallis convallis tellus id. At auctor urna nunc id. Elit pellentesque habitant morbi tristique senectus et netus et malesuada.\n" +
            "Feugiat in ante metus dictum at tempor commodo ullamcorper a. Risus nec feugiat in fermentum posuere urna nec tincidunt praesent. Ligula ullamcorper malesuada proin libero nunc consequat interdum. Scelerisque mauris pellentesque pulvinar pellentesque habitant morbi tristique senectus. Lorem ipsum dolor sit amet consectetur adipiscing elit ut aliquam. Nulla posuere sollicitudin aliquam ultrices sagittis orci a scelerisque purus. Tellus id interdum velit laoreet id donec. Quam elementum pulvinar etiam non quam lacus suspendisse faucibus interdum. Ultrices mi tempus imperdiet nulla malesuada pellentesque. In ante metus dictum at tempor commodo.\n" +
            "Augue lacus viverra vitae congue eu consequat ac felis. Integer malesuada nunc vel risus commodo viverra maecenas accumsan. Ac feugiat sed lectus vestibulum mattis ullamcorper. Neque convallis a cras semper auctor neque vitae tempus. Eget egestas purus viverra accumsan in nisl nisi scelerisque eu. In eu mi bibendum neque egestas congue quisque egestas. Orci phasellus egestas tellus rutrum tellus pellentesque eu. Eu consequat ac felis donec. Quam id leo in vitae turpis. Maecenas accumsan lacus vel facilisis volutpat est velit egestas. Tincidunt augue interdum velit euismod in pellentesque massa placerat duis. Viverra vitae congue eu consequat. Risus nullam eget felis eget. Odio euismod lacinia at quis risus sed vulputate odio ut.\n" +
            "Leo duis ut diam quam nulla porttitor. Pellentesque dignissim enim sit amet. In hac habitasse platea dictumst quisque sagittis purus sit amet. Nibh mauris cursus mattis molestie a iaculis at. Quam lacus suspendisse faucibus interdum posuere. Quisque egestas diam in arcu cursus euismod quis. Sem viverra aliquet eget sit amet tellus cras adipiscing enim. Facilisis magna etiam tempor orci eu. Lectus mauris ultrices eros in cursus turpis massa. Tellus cras adipiscing enim eu turpis egestas.\n" +
            "Dictumst vestibulum rhoncus est pellentesque elit ullamcorper. Turpis in eu mi bibendum neque egestas congue quisque. Odio tempor orci dapibus ultrices in iaculis nunc sed augue. Non arcu risus quis varius. Malesuada pellentesque elit eget gravida cum sociis natoque. Velit laoreet id donec ultrices. Ultricies lacus sed turpis tincidunt id aliquet risus. Malesuada fames ac turpis egestas integer eget aliquet nibh. Nulla facilisi nullam vehicula ipsum a arcu cursus vitae. Id velit ut tortor pretium viverra. Sit amet commodo nulla facilisi nullam vehicula. Vel pharetra vel turpis nunc eget lorem. Lobortis feugiat vivamus at augue eget arcu dictum varius. Est sit amet facilisis magna etiam tempor orci.\n" +
            "Eu lobortis elementum nibh tellus molestie. Dui id ornare arcu odio ut sem nulla pharetra. At risus viverra adipiscing at in tellus integer feugiat. Metus aliquam eleifend mi in nulla posuere. Dui vivamus arcu felis bibendum ut tristique et egestas. Eget arcu dictum varius duis at. Lectus urna duis convallis convallis tellus id interdum velit laoreet. Aenean et tortor at risus viverra adipiscing at. Quis vel eros donec ac odio tempor orci dapibus. Nulla at volutpat diam ut venenatis tellus in metus. Sed lectus vestibulum mattis ullamcorper velit sed ullamcorper. At varius vel pharetra vel. Tortor dignissim convallis aenean et. Egestas sed sed risus pretium quam. Feugiat nisl pretium fusce id velit ut tortor. Sit amet cursus sit amet dictum. In hac habitasse platea dictumst quisque sagittis purus sit. Velit egestas dui id ornare arcu. Ut diam quam nulla porttitor massa id neque aliquam. Lectus arcu bibendum at varius vel pharetra vel turpis nunc.\n" +
            "Libero volutpat sed cras ornare arcu dui vivamus. Fusce ut placerat orci nulla pellentesque. Nulla posuere sollicitudin aliquam ultrices sagittis orci a scelerisque. Nam libero justo laoreet sit amet cursus sit amet. Elementum facilisis leo vel fringilla. Tortor pretium viverra suspendisse potenti. Nec tincidunt praesent semper feugiat nibh sed pulvinar proin gravida. Mauris a diam maecenas sed enim ut. Praesent tristique magna sit amet. Laoreet id donec ultrices tincidunt arcu non. Congue quisque egestas diam in arcu cursus euismod quis. Iaculis nunc sed augue lacus viverra vitae congue. Sem nulla pharetra diam sit amet nisl suscipit. Pellentesque adipiscing commodo elit at imperdiet. At tellus at urna condimentum mattis pellentesque id nibh tortor. Mauris pellentesque pulvinar pellentesque habitant morbi.\n" +
            "Ac placerat vestibulum lectus mauris ultrices eros in cursus turpis. Mi eget mauris pharetra et. Turpis egestas integer eget aliquet nibh praesent tristique magna sit. Pulvinar neque laoreet suspendisse interdum consectetur. Vitae nunc sed velit dignissim sodales ut eu sem. Adipiscing bibendum est ultricies integer quis auctor. Enim ut tellus elementum sagittis vitae et leo duis. Venenatis a condimentum vitae sapien. Sed sed risus pretium quam vulputate dignissim suspendisse in est. Feugiat nibh sed pulvinar proin gravida hendrerit. Varius sit amet mattis vulputate enim nulla. Posuere lorem ipsum dolor sit amet consectetur adipiscing elit duis. Tellus pellentesque eu tincidunt tortor aliquam nulla facilisi cras. Erat nam at lectus urna duis convallis convallis. Viverra nibh cras pulvinar mattis nunc sed. Sit amet tellus cras adipiscing enim eu turpis. Dolor sit amet consectetur adipiscing elit. Aenean pharetra magna ac placerat vestibulum lectus mauris ultrices.\n" +
            "Est ullamcorper eget nulla facilisi etiam dignissim diam quis enim. Malesuada fames ac turpis egestas. A scelerisque purus semper eget duis at tellus at. Orci porta non pulvinar neque laoreet suspendisse interdum. Purus gravida quis blandit turpis cursus. Semper risus in hendrerit gravida. Ac felis donec et odio pellentesque diam volutpat. Arcu cursus vitae congue mauris rhoncus aenean vel. Quis imperdiet massa tincidunt nunc pulvinar. Malesuada fames ac turpis egestas sed tempus urna. Etiam sit amet nisl purus in. Sed blandit libero volutpat sed. Eget est lorem ipsum dolor sit. Non enim praesent elementum facilisis leo. Tellus in hac habitasse platea dictumst vestibulum rhoncus est pellentesque. Pellentesque diam volutpat commodo sed egestas egestas fringilla phasellus faucibus. Viverra nibh cras pulvinar mattis nunc sed blandit libero volutpat.\n" +
            "Mi proin sed libero enim sed faucibus. Ut tortor pretium viverra suspendisse potenti. Cursus euismod quis viverra nibh. Fusce ut placerat orci nulla pellentesque dignissim enim sit. Scelerisque viverra mauris in aliquam sem. Eu sem integer vitae justo eget magna. Consectetur adipiscing elit ut aliquam purus sit amet. Dictum at tempor commodo ullamcorper a lacus vestibulum sed. Morbi tristique senectus et netus. Convallis tellus id interdum velit. Tempor orci dapibus ultrices in iaculis nunc. Ac odio tempor orci dapibus ultrices.\n" +
            "Ac auctor augue mauris augue. Nulla at volutpat diam ut venenatis tellus. Sed cras ornare arcu dui vivamus arcu felis. Egestas sed tempus urna et pharetra pharetra. Sociis natoque penatibus et magnis dis. Morbi tristique senectus et netus et malesuada fames ac. Massa enim nec dui nunc mattis enim. Ipsum suspendisse ultrices gravida dictum fusce ut placerat. Amet aliquam id diam maecenas. Orci ac auctor augue mauris augue neque gravida in fermentum. Cursus turpis massa tincidunt dui ut ornare lectus sit amet. Feugiat in ante metus dictum at tempor commodo. Ut ornare lectus sit amet. Bibendum ut tristique et egestas quis. Ultrices in iaculis nunc sed augue lacus viverra vitae congue. Pulvinar sapien et ligula ullamcorper malesuada proin. Adipiscing elit pellentesque habitant morbi tristique senectus. Ultrices vitae auctor eu augue. Mi tempus imperdiet nulla malesuada pellentesque elit eget gravida.\n" +
            "Arcu risus quis varius quam quisque. Odio eu feugiat pretium nibh ipsum consequat nisl vel. Urna et pharetra pharetra massa massa ultricies mi. Lectus sit amet est placerat in egestas erat imperdiet. Massa eget egestas purus viverra accumsan in nisl nisi. Fames ac turpis egestas maecenas pharetra convallis posuere morbi. In massa tempor nec feugiat. Tincidunt arcu non sodales neque sodales ut etiam sit amet. Purus viverra accumsan in nisl nisi scelerisque eu ultrices vitae. Nunc sed blandit libero volutpat sed cras ornare arcu dui. Vitae aliquet nec ullamcorper sit amet risus. Aliquam sem fringilla ut morbi tincidunt augue interdum velit euismod. Et pharetra pharetra massa massa.\n" +
            "Sit amet cursus sit amet dictum sit. Duis at consectetur lorem donec. Pellentesque elit ullamcorper dignissim cras tincidunt. Risus viverra adipiscing at in tellus integer feugiat scelerisque varius. Dignissim convallis aenean et tortor at risus viverra adipiscing. Eu lobortis elementum nibh tellus molestie nunc non blandit massa. Nec nam aliquam sem et. Neque vitae tempus quam pellentesque nec nam aliquam sem. Justo donec enim diam vulputate ut pharetra sit. Mattis vulputate enim nulla aliquet porttitor. Pellentesque massa placerat duis ultricies lacus. Eget nunc scelerisque viverra mauris. Libero volutpat sed cras ornare arcu dui. Ut sem viverra aliquet eget sit amet tellus cras adipiscing. Egestas erat imperdiet sed euismod nisi.\n" +
            "Mauris nunc congue nisi vitae suscipit. Vitae justo eget magna fermentum iaculis eu non diam phasellus. Bibendum enim facilisis gravida neque convallis a cras semper. Tristique magna sit amet purus gravida quis. Porttitor rhoncus dolor purus non enim praesent elementum facilisis leo. Magna ac placerat vestibulum lectus. Massa sed elementum tempus egestas sed sed risus pretium quam. Vulputate dignissim suspendisse in est ante in nibh mauris cursus. Aenean sed adipiscing diam donec. Tellus at urna condimentum mattis pellentesque id nibh tortor id.";

    public static List<Note> generateNotes() {

        List<Note> notes = new ArrayList<>();

        Random random = new Random(SEED);

        Scanner scanner = new Scanner(TEXT);
        while(scanner.hasNextLine()) {
            Note note = new Note();
            note.setBody(scanner.nextLine());

            String[] words = note.getBody().split(" ");
            note.setTitle(words[0] + " " + words[1] + " " + words[2]);

            if(random.nextDouble() < PERCENT_WITH_REMINDER) {
                note.setHasReminder(true);
                note.setReminder(getRandomDate(REMINDER_START, MONTH_DURATION, random));
            }
            else
                note.setHasReminder(false);

            note.setCategory(Category.values()[random.nextInt(Category.values().length)]);

            note.setCreated(getRandomDate(CREATED_START, MONTH_DURATION, random));
            note.setModified(note.getCreated());

            notes.add(note);
        }

        return notes;
    }

    private static Date getRandomDate(Date start, long duration, Random random) {
        return new Date((long)(start.getTime() + (duration * random.nextDouble())));
    }

    public static List<User> generateUsers(Context ctx){
        List<User> users = new ArrayList<>();

        // AVATARS
        Bitmap jAvatar = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.j);
        Bitmap kAvatar = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.k);
        Bitmap wAvatar = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.w);
        Bitmap iAvatar = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.i);
        Bitmap mAvatar = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.m);

        // Users
        long defaultId = -1;
        users.add(new User(defaultId, "Jesse", jAvatar, "jessesafir@gmail.com"));
        users.add(new User(defaultId, "William", wAvatar, "willbuche@gmail.com"));
        users.add(new User(defaultId, "Kennedy", kAvatar, "kjb@gmail.com"));
        users.add(new User(defaultId, "Ian", iAvatar, "ianclement@gmail.com"));
        users.add(new User(defaultId, "Jim", mAvatar, "jmatthews@gmail.com"));

        return users;
    }

}
