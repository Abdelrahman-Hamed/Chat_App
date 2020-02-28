package org.asasna.chat.client.model.chatbot;

import org.asasna.chat.client.model.chatbot.ChatterBot;
import org.asasna.chat.client.model.chatbot.ChatterBotSession;
import org.asasna.chat.client.model.chatbot.ChatterBotThought;
import org.asasna.chat.client.model.chatbot.Utils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/*
    chatter-bot-api
    Copyright (C) 2011 pierredavidbelanger@gmail.com
 
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
class Pandorabots implements ChatterBot {
    private final String botid;

    public Pandorabots(String botid) {
        this.botid = botid;
    }

    @Override
    public org.asasna.chat.client.model.chatbot.ChatterBotSession createSession(Locale... locales) {
        return new Session();
    }

    private class Session implements ChatterBotSession {
        private final Map<String, String> vars;

        public Session() {
            vars = new LinkedHashMap<String, String>();
            vars.put("botid", botid);
            vars.put("custid", UUID.randomUUID().toString());
        }

        public org.asasna.chat.client.model.chatbot.ChatterBotThought think(org.asasna.chat.client.model.chatbot.ChatterBotThought thought) throws Exception {
            vars.put("input", thought.getText());

            String response = Utils.request("https://www.pandorabots.com/pandora/talk-xml", null, null, vars);

            org.asasna.chat.client.model.chatbot.ChatterBotThought responseThought = new org.asasna.chat.client.model.chatbot.ChatterBotThought();

            responseThought.setText(Utils.xPathSearch(response, "//result/that/text()"));

            return responseThought;
        }

        public String think(String text) throws Exception {
            org.asasna.chat.client.model.chatbot.ChatterBotThought thought = new ChatterBotThought();
            thought.setText(text);
            return think(thought).getText();
        }
    }
}