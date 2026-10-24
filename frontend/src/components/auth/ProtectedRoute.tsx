import type { ReactNode } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { getStoredUser } from "@/services/authService";

export default function ProtectedRoute({ children, roles = [] }: { children: ReactNode; roles?: string[] }) {
  const location = useLocation();
  const user = getStoredUser();
  if (!user) return <Navigate to="/login" replace state={{ from: location.pathname }} />;
  if (roles.length && !roles.some((role) => user.roles.includes(role))) return <Navigate to="/" replace />;
  return children;
}
