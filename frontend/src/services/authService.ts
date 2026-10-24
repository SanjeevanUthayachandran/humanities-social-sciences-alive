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

export async function loginWithGoogle(credential: string) {
  const response = await apiClient.post<ApiResponse<AuthenticationData>>("/auth/google", { credential });
  return response.data.data;
}

export function getAuthenticationDestination(authentication: AuthenticationData) {
  if (authentication.user.roles.includes("ADMIN")) return "/admin";
  if (authentication.user.roles.includes("INSTRUCTOR")) return "/instructor";
  return "/";
}

export async function register(payload: RegisterPayload) {
  const response = await apiClient.post<ApiResponse<string>>("/auth/register", payload);
  return response.data.data ?? response.data.message;
}

export async function requestPasswordReset(email: string) {
  const response = await apiClient.post<ApiResponse<string>>("/auth/forgot-password", { email });
  return response.data.data ?? response.data.message;
}

export async function resetPassword(token: string, newPassword: string) {
  const response = await apiClient.post<ApiResponse<string>>("/auth/reset-password", { token, newPassword });
  return response.data.data ?? response.data.message;
}

export async function changePassword(currentPassword: string, newPassword: string) {
  const response = await apiClient.post<ApiResponse<string>>("/auth/change-password", { currentPassword, newPassword });
  return response.data.data ?? response.data.message;
}

export function getStoredUser(): AuthenticationData["user"] | null {
  const value = localStorage.getItem("hssa_user") ?? sessionStorage.getItem("hssa_user");
  if (!value) return null;
  try {
    return JSON.parse(value) as AuthenticationData["user"];
  } catch {
    return null;
  }
}

export function clearAuthentication() {
  for (const storage of [localStorage, sessionStorage]) {
    storage.removeItem("hssa_access_token");
    storage.removeItem("hssa_refresh_token");
    storage.removeItem("hssa_user");
  }
  window.dispatchEvent(new Event("hssa-auth-changed"));
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
  window.dispatchEvent(new Event("hssa-auth-changed"));
}
