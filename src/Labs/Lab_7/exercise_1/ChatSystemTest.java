package Labs.Lab_7.exercise_1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String roomName) {
        super("No such room: " + roomName);
    }
}

class NoSuchUserException extends Exception {
    public NoSuchUserException(String userName) {
        super("No such user: " + userName);

    }
}

class ChatRoom {
    String room;
    Set<String> usersInRoom;

    public ChatRoom(String room) {
        this.room = room;
        usersInRoom = new TreeSet<>();
    }

    public String getRoom() {
        return room;
    }

    public void addUser(String username) {
        usersInRoom.add(username);
    }

    public boolean removeUser(String username) {
        return usersInRoom.remove(username);
    }

    public boolean hasUser(String username) {
        return usersInRoom.contains(username);
    }

    public int numUsers() {
        return usersInRoom.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(room).append("\n");

        if (usersInRoom.isEmpty())
            sb.append("EMPTY\n");
        else
            usersInRoom.forEach(user -> sb.append(user).append("\n"));

        return sb.toString();
    }
}

class ChatSystem {
    TreeMap<String, ChatRoom> rooms;
    Set<String> registeredUsers;

    public ChatSystem() {
        rooms = new TreeMap<>();
        registeredUsers = new HashSet<>();
    }

    public void addRoom(String roomName) {
        rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (!rooms.containsKey(roomName))
            throw new NoSuchRoomException(roomName);

        return rooms.get(roomName);
    }

    public void register(String userName) {
        registeredUsers.add(userName);
        rooms.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(ChatRoom::numUsers)))
                .findFirst()
                .get().getValue().addUser(userName);


    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!rooms.containsKey(roomName))
            throw new NoSuchRoomException(roomName);
        if (!registeredUsers.contains(userName))
            throw new NoSuchUserException(userName);

        rooms.get(roomName).addUser(userName);
    }

    public void registerAndJoin(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        registeredUsers.add(userName);
        rooms.get(roomName).addUser(userName);
    }

    public void leaveRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!rooms.containsKey(roomName))
            throw new NoSuchRoomException(roomName);

        boolean b = rooms.get(roomName).removeUser(userName);
        if (!b) throw new NoSuchUserException(userName);
    }

    public void followFriend(String username, String friend_username) throws NoSuchUserException {
        if (!registeredUsers.contains(friend_username))
            throw new NoSuchUserException(friend_username);
        if (!registeredUsers.contains(username))
            throw new NoSuchUserException(username);

        rooms.values().stream()
                .filter(value -> value.hasUser(friend_username))
                .forEach(value -> value.addUser(username));
    }


}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    try {
                        System.out.println(cs.getRoom(jin.next()) + "\n");
                    } catch (NoSuchRoomException ignored) {

                    }
                    continue;
                }
                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        Object[] params = new String[m.getParameterTypes().length];
                        for (int i = 0; i < params.length; ++i) params[i] = jin.next();
                        try {
                            m.invoke(cs, params);
                        } catch (IllegalAccessException | InvocationTargetException ignored) {

                        }
                    }
                }
            }
        }
    }

}
