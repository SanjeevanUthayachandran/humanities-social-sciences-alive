import apiClient from "@/services/apiClient";

export interface ContactPayload {
  fullName: string;
  email: string;
  phoneNumber?: string;
  subject: string;
  inquiryType: string;
  message: string;
}

export async function sendContactMessage(payload: ContactPayload) {
  await apiClient.post("/contact/messages", payload);
}

export async function subscribeToNewsletter(email: string) {
  await apiClient.post("/newsletter/subscribe", { email });
}
