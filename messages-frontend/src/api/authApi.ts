import type { LoginRequest, LoginResponse } from "../types/login";
import axiosClient from "./axiosClient";

export const loginUser = async ( data: LoginRequest ): Promise<LoginResponse> => {
    const response = await axiosClient.post("/auth/login", data);
    return response.data;
};