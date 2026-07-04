import { courses, events, newsItems, researchItems, workshops } from "@/services/contentService";

export interface SiteSearchResult {
  id: string;
  title: string;
  type: string;
  summary: string;
  path: string;
}

export function buildSearchIndex(): SiteSearchResult[] {
  return [
    ...courses.map((course) => ({
      id: course.id,
      title: course.title,
      type: "Course",
      summary: course.summary,
      path: `/courses/${course.slug}`,
    })),
    ...researchItems.map((item) => ({
      id: item.id,
      title: item.title,
      type: "Research",
      summary: item.summary,
      path: `/research/${item.slug}`,
    })),
    ...workshops.map((workshop) => ({
      id: workshop.id,
      title: workshop.title,
      type: "Workshop",
      summary: workshop.summary,
      path: "/workshops",
    })),
    ...events.map((event) => ({
      id: event.id,
      title: event.title,
      type: "Event",
      summary: event.summary,
      path: "/events",
    })),
    ...newsItems.map((item) => ({
      id: item.id,
      title: item.title,
      type: "News",
      summary: item.summary,
      path: "/news",
    })),
  ];
}

export function filterSearchResults(results: SiteSearchResult[], query: string) {
  const normalizedQuery = query.trim().toLowerCase();

  if (!normalizedQuery) {
    return results.slice(0, 8);
  }

  return results.filter((result) =>
    [result.title, result.type, result.summary]
      .join(" ")
      .toLowerCase()
      .includes(normalizedQuery),
  );
}
