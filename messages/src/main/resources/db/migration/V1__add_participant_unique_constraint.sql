ALTER TABLE conversation_participants
ADD CONSTRAINT uk_conversation_participant
UNIQUE (conversation_id, user_id);