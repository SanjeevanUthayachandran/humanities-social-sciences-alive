import { useContext } from "react";
import { ContentContext } from "@/context/contentContextValue";

export function useContent() {
  const value = useContext(ContentContext);
  if (!value) throw new Error("useContent must be used within ContentProvider");
  return value;
}
