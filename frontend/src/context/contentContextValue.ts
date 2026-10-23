import { createContext } from "react";
import type { Course, EventItem, FaqItem, ResearchItem, Workshop } from "@/types/content";

export interface ContentState {
  courses: Course[];
  researchItems: ResearchItem[];
  workshops: Workshop[];
  events: EventItem[];
  faqs: FaqItem[];
  isLoading: boolean;
  isUsingFallback: boolean;
}

export const ContentContext = createContext<ContentState | null>(null);
