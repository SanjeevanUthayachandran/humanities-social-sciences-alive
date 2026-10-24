import apiClient from "@/services/apiClient";
import type { ApiResponse, PageResponse } from "@/types/api";

export interface ManagementCourse {
  id: string;
  title: string;
  slug: string;
  subtitle?: string;
  categoryName?: string;
  level: string;
  status: string;
  isFree: boolean;
  price?: number;
  currency?: string;
  cardImageUrl?: string;
}

export interface CourseCategory {
  id: string;
  name: string;
  slug: string;
  description?: string;
}

export interface CreateCoursePayload {
  categoryId: string;
  title: string;
  subtitle: string;
  description: string;
  level: string;
  visibility: string;
  isFree: boolean;
  price?: number;
  discountedPrice?: number;
  currency: string;
  cardImageUrl: string;
  certificationAvailable: boolean;
  tags: string[];
}

export interface ProgramPayload {
  title: string;
  description: string;
  eventType: string;
  startTime: string;
  endTime: string;
  locationType: string;
  locationDetails: string;
  timezone: string;
  maxParticipants: number;
  isFree: boolean;
  price?: number;
  coverMediaId?: string;
}

export interface CourseStudent {
  enrollmentId: string;
  userId: string;
  studentName: string;
  studentEmail: string;
  status: string;
  enrolledAt: string;
}

export interface UploadedMedia {
  id: string;
  originalName: string;
  fileSize: number;
  width?: number;
  height?: number;
}

export async function fetchCourseCategories() {
  const response = await apiClient.get<CourseCategory[]>("/course-categories");
  return response.data;
}

export async function fetchAdminCourses() {
  const response = await apiClient.post<ApiResponse<PageResponse<ManagementCourse>>>("/courses/search?size=100&sort=updatedAt,desc", {});
  return response.data.data.content;
}

export async function fetchInstructorCourses() {
  const response = await apiClient.get<ApiResponse<ManagementCourse[]>>("/instructor/courses");
  return response.data.data;
}

export async function createCourse(payload: CreateCoursePayload) {
  const response = await apiClient.post<ApiResponse<ManagementCourse>>("/courses", payload);
  return response.data.data;
}

export async function publishCourse(courseId: string, publish = true) {
  await apiClient.patch(`/courses/${courseId}/publish`, { publish });
}

export async function createProgram(type: "workshops" | "events", payload: ProgramPayload) {
  const response = await apiClient.post(`/${type}`, payload);
  return response.data;
}

export async function assignInstructor(email: string, courseId: string, primary: boolean) {
  const response = await apiClient.post<ApiResponse<string>>("/admin/course-access/instructors", { email, courseId, primary });
  return response.data.data ?? response.data.message;
}

export async function fetchUsers() {
  const response = await apiClient.get<ApiResponse<PageResponse<{ id: string; email: string; fullName: string; isActive: boolean }>>>("/admin/users?size=100");
  return response.data.data.content;
}

export async function updateCoursePaymentSettings(courseId: string, settings: {
  gatewayCode: string;
  onlinePaymentEnabled: boolean;
  bankTransferEnabled: boolean;
  installmentsEnabled: boolean;
}) {
  const response = await apiClient.put(`/admin/course-payments/${courseId}`, settings);
  return response.data;
}

export async function fetchCourseStudents(courseId: string) {
  const response = await apiClient.get<ApiResponse<PageResponse<CourseStudent>>>(
    `/courses/${encodeURIComponent(courseId)}/students?size=100&sort=enrolledAt,desc`,
  );
  return response.data.data.content;
}

export async function uploadCardImage(file: File, altText: string) {
  const data = new FormData();
  data.append("file", file);
  data.append("isPublic", "true");
  data.append("altText", altText);
  const response = await apiClient.post<ApiResponse<UploadedMedia>>("/media/upload", data, {
    headers: { "Content-Type": "multipart/form-data" },
    timeout: 30_000,
  });
  return response.data.data;
}

export function mediaDownloadUrl(mediaId: string) {
  const base = String(apiClient.defaults.baseURL ?? "").replace(/\/$/, "");
  return `${base}/media/download/${mediaId}`;
}
