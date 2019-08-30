package com.wxj.client.service;

import com.wxj.util.CommUtils;
import com.wxj.vo.MessageVO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CreateGroupGUI {
    private JPanel createGroupPanel;
    private JPanel friendLabelPanel;
    private JTextField groupNameText;
    private JButton conformBtn;

    private String myName;
    private Set<String> friends;
    private Connect2Server connect2Server;
    private FriendsList friendsList;


    public CreateGroupGUI(String myName,Set<String> friends,
                          Connect2Server connect2Server,FriendsList friendsList) {
        this.myName = myName;
        this.friends = friends;
        this.connect2Server = connect2Server;
        this.friendsList = friendsList;
        JFrame frame = new JFrame("创建群组");
        frame.setContentPane(createGroupPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);
        //frame.pack();
        frame.setVisible(true);
        //将在线好友以checkBox展示到界面中
        friendLabelPanel.setLayout(new BoxLayout(friendLabelPanel,BoxLayout.Y_AXIS));  //标签纵向排列
        Iterator<String> iterator = friends.iterator();
        while(iterator.hasNext()){

            String labelName = iterator.next();
            JCheckBox checkBox = new JCheckBox(labelName);
            friendLabelPanel.add(checkBox);
        }
        friendLabelPanel.revalidate(); //刷新

        //点击提交按钮提交信息到服务端
        conformBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //1.判断哪些好友选中加入群聊
                Set<String> selectedFriends = new HashSet<>();
                Component[] comps = friendLabelPanel.getComponents(); //获取这个Panel里面的所有组件
                for(Component comp : comps){
                    JCheckBox checkBox = (JCheckBox) comp;
                    if(checkBox.isSelected()){//标签被选中
                        String labelName = checkBox.getText(); //获取标签内容
                        selectedFriends.add(labelName);
                    }
                }
                selectedFriends.add(myName);  //将自己添加进来
                //2.获取群名输入框输入的群名称
                String groupName = groupNameText.getText();
                //3.将群名+选中好友信息发送到服务器
                // type:3
                // content:groupName
                // to:[user1,user2,user3 ...]
                MessageVO messageVO = new MessageVO();
                messageVO.setType("3");
                messageVO.setContent(groupName);
                messageVO.setTo(CommUtils.object2Json(selectedFriends));
                try {
                    PrintStream out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
                    out.println(CommUtils.object2Json(messageVO));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                //4.将当前创建群界面隐藏，刷新好友列表界面的群列表
                frame.setVisible(false);
                //addGroupInfo
                //loadGroup
                friendsList.addGroup(groupName,selectedFriends);
                friendsList.loadGroupList();
            }
        });
    }
}
