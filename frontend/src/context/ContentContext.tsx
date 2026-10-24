import { useEffect, useMemo, useState, type ReactNode } from "react";
import { ContentContext, type ContentState } from "@/context/contentContextValue";
import {
  fetchCourses,
  fetchBooks,
  fetchContactOffices,
  fetchEvents,
  fetchFaqs,
  fetchPublicMetrics,
  fetchPublications,
  fetchResearch,
  fetchWorkshops,
} from "@/services/contentApi";
import type { PublicMetrics } from "@/types/content";

const EMPTY_METRICS: PublicMetrics = {
  totalCourses: 0,
  totalStudents: 0,
  totalInstructors: 0,
  totalEvents: 0,
};

export function ContentProvider({ children }: { children: ReactNode }) {
  const [courses, setCourses] = useState<ContentState["courses"]>([]);
  const [researchItems, setResearchItems] = useState<ContentState["researchItems"]>([]);
  const [workshops, setWorkshops] = useState<ContentState["workshops"]>([]);
  const [events, setEvents] = useState<ContentState["events"]>([]);
  const [publications, setPublications] = useState<ContentState["publications"]>([]);
  const [books, setBooks] = useState<ContentState["books"]>([]);
  const [offices, setOffices] = useState<ContentState["offices"]>([]);
  const [faqs, setFaqs] = useState<ContentState["faqs"]>([]);
  const [metrics, setMetrics] = useState(EMPTY_METRICS);
  const [isLoading, setIsLoading] = useState(true);
  const [failedResources, setFailedResources] = useState<string[]>([]);

  useEffect(() => {
    let active = true;

    const resourceNames = ["courses", "research", "workshops", "events", "publications", "books", "offices", "FAQs", "statistics"];

    Promise.allSettled([
      fetchCourses(),
      fetchResearch(),
      fetchWorkshops(),
      fetchEvents(),
      fetchPublications(),
      fetchBooks(),
      fetchContactOffices(),
      fetchFaqs(),
      fetchPublicMetrics(),
    ])
      .then((results) => {
        if (!active) return;
        const [courseResult, researchResult, workshopResult, eventResult, publicationResult, bookResult, officeResult, faqResult, metricsResult] = results;

        if (courseResult.status === "fulfilled") setCourses(courseResult.value);
        if (researchResult.status === "fulfilled") setResearchItems(researchResult.value);
        if (workshopResult.status === "fulfilled") setWorkshops(workshopResult.value);
        if (eventResult.status === "fulfilled") setEvents(eventResult.value);
        if (publicationResult.status === "fulfilled") setPublications(publicationResult.value);
        if (bookResult.status === "fulfilled") setBooks(bookResult.value);
        if (officeResult.status === "fulfilled") setOffices(officeResult.value);
        if (faqResult.status === "fulfilled") setFaqs(faqResult.value);
        if (metricsResult.status === "fulfilled") setMetrics(metricsResult.value);
        setFailedResources(
          results.flatMap((result, index) => result.status === "rejected" ? [resourceNames[index]] : []),
        );
      })
      .finally(() => {
        if (active) setIsLoading(false);
      });

    return () => {
      active = false;
    };
  }, []);

  const value = useMemo(
    () => ({
      courses,
      researchItems,
      workshops,
      events,
      publications,
      books,
      offices,
      faqs,
      metrics,
      isLoading,
      failedResources,
    }),
    [courses, researchItems, workshops, events, publications, books, offices, faqs, metrics, isLoading, failedResources],
  );

  return <ContentContext.Provider value={value}>{children}</ContentContext.Provider>;
}
