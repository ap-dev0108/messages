import { ConversationResponse } from "../types/conversation";
import axiosClient from "./axiosClient";

export const getConversations = async (): Promise<ConversationResponse[]> => {
    const response = await axiosClient.get("/conversations");

    return response.data.data;
};