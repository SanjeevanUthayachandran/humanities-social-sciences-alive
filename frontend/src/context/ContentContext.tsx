import { useEffect, useMemo, useState, type ReactNode } from "react";
import { ContentContext } from "@/context/contentContextValue";
import {
  fetchCourses,
  fetchEvents,
  fetchFaqs,
  fetchResearch,
  fetchWorkshops,
} from "@/services/contentApi";
import {
  courses as fallbackCourses,
  events as fallbackEvents,
  faqs as fallbackFaqs,
  researchItems as fallbackResearch,
  workshops as fallbackWorkshops,
} from "@/services/contentService";

export function ContentProvider({ children }: { children: ReactNode }) {
  const [courses, setCourses] = useState(fallbackCourses);
  const [researchItems, setResearchItems] = useState(fallbackResearch);
  const [workshops, setWorkshops] = useState(fallbackWorkshops);
  const [events, setEvents] = useState(fallbackEvents);
  const [faqs, setFaqs] = useState(fallbackFaqs);
  const [isLoading, setIsLoading] = useState(true);
  const [isUsingFallback, setIsUsingFallback] = useState(false);

  useEffect(() => {
    let active = true;

    Promise.allSettled([fetchCourses(), fetchResearch(), fetchWorkshops(), fetchEvents(), fetchFaqs()])
      .then((results) => {
        if (!active) return;
        const [courseResult, researchResult, workshopResult, eventResult, faqResult] = results;

        if (courseResult.status === "fulfilled") setCourses(courseResult.value);
        if (researchResult.status === "fulfilled") setResearchItems(researchResult.value);
        if (workshopResult.status === "fulfilled") setWorkshops(workshopResult.value);
        if (eventResult.status === "fulfilled") setEvents(eventResult.value);
        if (faqResult.status === "fulfilled") setFaqs(faqResult.value);
        setIsUsingFallback(results.some((result) => result.status === "rejected"));
      })
      .finally(() => {
        if (active) setIsLoading(false);
      });

    return () => {
      active = false;
    };
  }, []);

  const value = useMemo(
    () => ({ courses, researchItems, workshops, events, faqs, isLoading, isUsingFallback }),
    [courses, researchItems, workshops, events, faqs, isLoading, isUsingFallback],
  );

  return <ContentContext.Provider value={value}>{children}</ContentContext.Provider>;
}
