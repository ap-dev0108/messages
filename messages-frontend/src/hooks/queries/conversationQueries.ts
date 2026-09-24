import { useQuery } from "@tanstack/react-query";
import type { ConversationResponse } from "../../types/conversation";
import { getConversations } from "../../api/conversationApi";

export const useConversations = () => {
    return useQuery<ConversationResponse[]>({
        queryKey: ["conversations"],
        queryFn: getConversations,
    });
};