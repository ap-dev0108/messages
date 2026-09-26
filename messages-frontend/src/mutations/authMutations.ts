import { useMutation } from "@tanstack/react-query";
import {loginUser} from "../api/authApi";
import type { LoginRequest, LoginResponse } from "../types/login";
import {setTokens} from "../services/authStorage";

export const useLogin = () => {
    return useMutation<LoginResponse, Error, LoginRequest>({
        mutationFn: loginUser,

        onSuccess: (data) => {
            console.log("Login successful:", data);
            setTokens(data.accessToken, data.refreshToken);
        }
    });
};