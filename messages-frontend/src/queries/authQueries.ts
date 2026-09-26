import { useQuery } from "@tanstack/react-query";
import { type UserResponse } from "../types/user";
import { getCurrentUser } from "../api/authApi";
import { getAccessToken } from "../services/authStorage";

export const useCurrentUser = () => {
    const accessToken = getAccessToken();

    return useQuery<UserResponse, Error>({
        queryKey: ["currentUser"],
        queryFn: getCurrentUser,
        enabled: !!accessToken,
        retry: false
    });
}