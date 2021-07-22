package com.general_hello.commands.commands.DefaultCommands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    public static ArrayList<Long> queueMessageId = new ArrayList<>();
    public static ArrayList<Member> firstGameMemberQueue = new ArrayList<>();
    public static ArrayList<Member> secondGameMemberQueue = new ArrayList<>();
    public static HashMap<User, ArrayList<String>> userDetails = new HashMap<>();
    public static boolean openQueue = true;
    public static HashMap<User, Integer> usersElo = new HashMap<>();
    public static ArrayList<Member> firstEmojiMember1 = new ArrayList<>();
    public static ArrayList<Member> secondEmojiMember1 = new ArrayList<>();
    public static ArrayList<Member> firstEmojiMember = new ArrayList<>();
    public static ArrayList<Member> secondEmojiMember = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<Member> thirdEmojiMember = new ArrayList<>();
    public static ArrayList<Member> fourthEmojiMember = new ArrayList<>();
    public static HashMap<User, Integer> progressInQuestioning = new HashMap<>();
    public static HashMap<Member, Integer> voteCount = new HashMap<>();
    public static HashMap<Member, TextChannel> textChannelsToFirstMember = new HashMap<>();

}