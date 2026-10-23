import apiClient from "@/services/apiClient";
import type { ApiResponse, AuthenticationData } from "@/types/api";

export interface LoginPayload {
  email: string;
  password: string;
}

export interface RegisterPayload extends LoginPayload {
  firstName: string;
  lastName: string;
}

export async function login(payload: LoginPayload) {
  const response = await apiClient.post<ApiResponse<AuthenticationData>>("/auth/login", payload);
  return response.data.data;
}

export async function register(payload: RegisterPayload) {
  const response = await apiClient.post<ApiResponse<string>>("/auth/register", payload);
  return response.data.data ?? response.data.message;
}

export function storeAuthentication(authentication: AuthenticationData, remember: boolean) {
  const storage = remember ? localStorage : sessionStorage;
  const otherStorage = remember ? sessionStorage : localStorage;

  otherStorage.removeItem("hssa_access_token");
  otherStorage.removeItem("hssa_refresh_token");
  otherStorage.removeItem("hssa_user");
  storage.setItem("hssa_access_token", authentication.accessToken);
  storage.setItem("hssa_refresh_token", authentication.refreshToken);
  storage.setItem("hssa_user", JSON.stringify(authentication.user));
}
