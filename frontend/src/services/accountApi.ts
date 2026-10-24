import apiClient from "@/services/apiClient";
import type { ApiResponse, PageResponse } from "@/types/api";

export interface UserProfile {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  displayName?: string;
  phoneNumber?: string;
  title?: string;
  biography?: string;
}

export interface StudentCourse {
  enrollmentId: string;
  courseId: string;
  courseTitle: string;
  courseSlug: string;
  status: string;
  enrolledAt: string;
}

export interface ProgramRegistration {
  id: string;
  status: string;
  registeredAt: string;
  startTime?: string;
  eventId?: string;
  eventTitle?: string;
  workshopId?: string;
  workshopTitle?: string;
}

export async function fetchProfile() {
  const response = await apiClient.get<ApiResponse<UserProfile>>("/profile");
  return response.data.data;
}

export async function updateProfile(payload: Partial<UserProfile>) {
  const response = await apiClient.put<ApiResponse<UserProfile>>("/profile", payload);
  return response.data.data;
}

export async function fetchMyCourses(userId: string) {
  const response = await apiClient.get<ApiResponse<PageResponse<StudentCourse>>>(`/users/${encodeURIComponent(userId)}/courses?size=100&sort=enrolledAt,desc`);
  return response.data.data.content;
}

export async function fetchMyEvents() {
  const response = await apiClient.get<ProgramRegistration[]>("/events/registrations/me");
  return response.data;
}

export async function fetchMyWorkshops() {
  const response = await apiClient.get<ProgramRegistration[]>("/workshops/registrations/me");
  return response.data;
}
