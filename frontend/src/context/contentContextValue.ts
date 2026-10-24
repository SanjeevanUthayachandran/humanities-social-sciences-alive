import { createContext } from "react";
import type {
  BookItem,
  ContactOffice,
  Course,
  EventItem,
  FaqItem,
  PublicationItem,
  PublicMetrics,
  ResearchItem,
  Workshop,
} from "@/types/content";

export interface ContentState {
  courses: Course[];
  researchItems: ResearchItem[];
  workshops: Workshop[];
  events: EventItem[];
  publications: PublicationItem[];
  books: BookItem[];
  offices: ContactOffice[];
  faqs: FaqItem[];
  metrics: PublicMetrics;
  isLoading: boolean;
  failedResources: string[];
}

export const ContentContext = createContext<ContentState | null>(null);
