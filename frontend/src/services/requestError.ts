import axios from "axios";

interface ErrorPayload {
  message?: string;
  data?: {
    message?: string;
    validationErrors?: Array<{ message?: string }>;
  };
}

export function getRequestErrorMessage(error: unknown, fallback: string) {
  if (!axios.isAxiosError<ErrorPayload>(error)) {
    return error instanceof Error ? error.message : fallback;
  }

  if (!error.response) {
    return error.code === "ECONNABORTED"
      ? "The server took too long to respond."
      : "Cannot connect to the HSSA server. Start the backend and try again.";
  }

  const payload = error.response?.data;
  return payload?.data?.validationErrors?.[0]?.message
    ?? payload?.data?.message
    ?? payload?.message
    ?? fallback;
}
