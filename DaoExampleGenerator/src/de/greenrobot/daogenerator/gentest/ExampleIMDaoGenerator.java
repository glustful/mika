/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.ContentProvider;
import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Generates entities and DAOs for the example project DaoExample.
 * 
 * Run it as a Java application (not Android).
 * 
 * @author Markus
 */
public class ExampleIMDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.yxtech.example.im.db");
        addImMessageMember(schema);
    	new DaoGenerator().generateAll(schema, "../DaoExampleGenerator/src-gen");
        
    }

    private static void addImMessageMember(Schema schema) {
    	Entity member = schema.addEntity("Member");
    	member.addIdProperty();
    	
    	member.addStringProperty("Uid");
    	Property username = member.addStringProperty("UserName").getProperty();
    	member.addStringProperty("NickName");
    	member.addStringProperty("PYInitial");
    	member.addStringProperty("PYQuanPin");
    	member.addStringProperty("HeadImgUrl");
    	member.addIntProperty("Sex");
    	member.addStringProperty("Signature");
    	member.addStringProperty("RemarkName");
    	member.addStringProperty("RemarkPYInitial");
    	member.addStringProperty("RemarkPYQuanPin");
    	member.addStringProperty("Province");
    	member.addStringProperty("City");
    	member.addStringProperty("Alias");
    	member.addStringProperty("StarFriend");
    	

    	
    	Entity message = schema.addEntity("Message");
    	message.addIdProperty();
    	
    	message.addLongProperty("Mid");
    	message.addLongProperty("Gid");
    	
    	message.addStringProperty("MsgId");
    	message.addStringProperty("ClientMsgId");
    	message.addStringProperty("FromUserName");
    	message.addStringProperty("ToUserName");
    	message.addIntProperty("MsgType");
    	message.addStringProperty("Content");
    	message.addStringProperty("ObjectContent");
    	message.addLongProperty("CreateTime");
    	message.addStringProperty("MediaId");
    	message.addStringProperty("Url");
    	
    	message.addStringProperty("extLocalUserName");
    	Property extRemoteUserName = message.addStringProperty("extRemoteUserName").notNull().getProperty();
    	message.addStringProperty("extRemoteDisplayName");
    	message.addIntProperty("extInOut");
    	message.addLongProperty("extTime");
    	message.addIntProperty("extRead");
    	message.addIntProperty("extStatus");
    	
    	
    	
//    	ToOne messageToMember = message.addToOne(member, extRemoteUserName);
//    	ToMany memberToMessages = member.addToMany(message, extRemoteUserName);
    	
    	
    	
    	ContentProvider cpMember = member.addContentProvider();
    	cpMember.setJavaPackage("com.yxtech.example.im.db");
    	cpMember.setAuthority("com.yxtech.example.im.db.Member");
    	cpMember.setBasePath("Member");
    	cpMember.setClassName("MemberContentProvider");
    	
    	ContentProvider cpMessage = message.addContentProvider();
    	cpMessage.setJavaPackage("com.yxtech.example.im.db");
    	cpMessage.setAuthority("com.yxtech.example.im.db.Message");
    	cpMessage.setBasePath("Message");
    	cpMessage.setClassName("MessageContentProvider");
    }
}
