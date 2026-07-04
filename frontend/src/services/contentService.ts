import aboutData from "@/services/mock/about.json";
import courseData from "@/services/mock/courses.json";
import eventData from "@/services/mock/events.json";
import faqData from "@/services/mock/faqs.json";
import newsData from "@/services/mock/news.json";
import researchData from "@/services/mock/research.json";
import searchData from "@/services/mock/search.json";
import workshopData from "@/services/mock/workshops.json";
import type {
  AboutContent,
  Course,
  EventItem,
  FaqItem,
  NewsItem,
  ResearchItem,
  SearchTopic,
  Workshop,
} from "@/types/content";

export const about = aboutData as AboutContent;
export const courses = courseData as Course[];
export const researchItems = researchData as ResearchItem[];
export const workshops = workshopData as Workshop[];
export const events = eventData as EventItem[];
export const newsItems = newsData as NewsItem[];
export const faqs = faqData as FaqItem[];
export const recentSearches = searchData.recent as SearchTopic[];
export const popularTopics = searchData.popular as SearchTopic[];

export function getCourseBySlug(slug: string | undefined) {
  return courses.find((course) => course.slug === slug);
}

export function getResearchBySlug(slug: string | undefined) {
  return researchItems.find((item) => item.slug === slug);
}

export function getFeaturedCourses() {
  return courses.filter((course) => course.featured);
}

export function getFeaturedResearch() {
  return researchItems.filter((item) => item.featured);
}

export function getUpcomingWorkshops() {
  return workshops.filter((workshop) => workshop.status === "Upcoming");
}

export function getUpcomingEvents() {
  return events;
}

export function getLatestNews() {
  return [...newsItems].sort((a, b) => b.date.localeCompare(a.date));
}
