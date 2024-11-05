export type Message = {
  role: string; 
  content: string; 
};

export type Conversation = {
  id: string;
  title: string;
  messages: Message[];
};
