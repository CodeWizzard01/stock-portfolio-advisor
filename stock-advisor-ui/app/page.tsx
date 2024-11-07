"use client";

import React, { useState } from "react";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from "../components/ui/card";
import Typography from "../components/ui/typography";
import { Button } from "../components/ui/button";
import {  Send } from "lucide-react";
import { Input } from "../components/ui/input";
import { sendMessage } from "./actions";


function ChatHome() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState<
    { user: string; html: string; time: string }[]
  >([]);
  const inputLength = input.trim().length;

  const handleSendMessage = async () => {
    try {
      const html = await sendMessage(input);
      const time = new Date().toLocaleTimeString();
      setMessages((prevMessages) => [
        { user: "You", html: input, time },
        { user: "Stock Advisor", html, time },
        ...prevMessages,
      ]);
      setInput("");
    } catch (error) {
      console.error("Error sending message:", error);
    }
  };

    const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
      if (event.key === "Enter" && inputLength > 0) {
        handleSendMessage();
      }
    };

  return (
    <div>
      <Card>
        <CardHeader>
          <Typography element="h4" as="h4">
            Chat
          </Typography>
        </CardHeader>
        <CardFooter>
          <Input
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Type your message..."
            className="m-2"
            onKeyDown={handleKeyDown}
          />
          <Button onClick={handleSendMessage} disabled={inputLength === 0}>
            <Send />
          </Button>
        </CardFooter>
        <CardContent>
          {messages.map((message, index) => (
            <div key={index} className="border-b border-gray-500 py-2">
              <Typography element="h5" as="h5">
                {message.user} ({message.time}):
              </Typography>
              <div dangerouslySetInnerHTML={{ __html: message.html }} />
            </div>
          ))}
        </CardContent>
      </Card>
    </div>
  );
}


export default ChatHome;
