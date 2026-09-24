import { useConversations } from "../hooks/queries/conversationQueries";

const Chat = () => {
  const { data: conversations, isLoading, isError, error } = useConversations();

  if (isLoading) {
    return <p>Loading conversations...</p>;
  }

  if (isError) {
    return <p>Error: {error.message}</p>;
  }

  if (!conversations || conversations.length === 0) {
    return <p>No conversations found.</p>;
  }

  return (
    <div>
      <h1>Conversations</h1>

      {conversations.map((conversation) => (
        <div key={conversation.id}>
          <p>ID: {conversation.id}</p>

          <p>Type: {conversation.type}</p>

          <p>Other user: {conversation.otherUser?.username ?? "None"}</p>

          <p>Last message: {conversation.message?.content ?? "No messages"}</p>

          <hr />
        </div>
      ))}
    </div>
  );
};

export default Chat;
