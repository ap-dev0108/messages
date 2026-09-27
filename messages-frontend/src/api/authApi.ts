import type { LoginRequest, LoginResponse } from "../types/login";
import type { UserResponse } from "../types/user";
import axiosClient from "./axiosClient";

export const loginUser = async ( data: LoginRequest ): Promise<LoginResponse> => {
    const response = await axiosClient.post("/auth/login", data);
    return response.data;
};

export const getCurrentUser = async (): Promise<UserResponse> => {
    const response = await axiosClient.get("/Users/me");

    return response.data.data;
};