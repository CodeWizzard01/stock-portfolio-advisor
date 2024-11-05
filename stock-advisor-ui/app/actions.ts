"use server";

import { marked } from "marked";


export async function sendMessage(userMessage: string): Promise<string> {
  const response = await fetch(
    `http://localhost:8080/chat?userMessage=${encodeURIComponent(userMessage)}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }

  const markdown = await response.text();
  const html = marked(markdown);

  return html;
}