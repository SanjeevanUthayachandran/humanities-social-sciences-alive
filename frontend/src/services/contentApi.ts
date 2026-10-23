import apiClient from "@/services/apiClient";
import type { ApiResponse, PageResponse } from "@/types/api";
import type { Course, EventItem, FaqItem, ResearchItem, Workshop } from "@/types/content";

interface CourseSummaryDto {
  id: string;
  title: string;
  slug: string;
  subtitle?: string;
  description?: string;
  categoryName?: string;
  level?: string;
  isFree?: boolean;
  price?: number;
  discountedPrice?: number;
  currency?: string;
  durationHours?: number;
  tags?: string[];
}

interface CourseDetailDto extends CourseSummaryDto {
  language?: string;
}

interface ResearchSummaryDto {
  id: string;
  title: string;
  slug: string;
  abstractText?: string;
  categoryName?: string;
  researchType?: string;
  publishedDate?: string;
}

interface ResearchDetailDto extends ResearchSummaryDto {
  content?: string;
  keywords?: string[];
  members?: Array<{ firstName: string; lastName: string; isPrimary: boolean }>;
}

interface ProgramSummaryDto {
  id: string;
  title: string;
  description?: string;
  eventType?: string;
  status?: string;
  locationType?: string;
  locationDetails?: string;
  startTime: string;
  endTime?: string;
  isFree?: boolean;
  price?: number;
  maxParticipants?: number;
  organizerName?: string;
}

interface FaqDto extends FaqItem {
  id: string;
  displayOrder: number;
}

function readableEnum(value: string | undefined, fallback: string) {
  if (!value) return fallback;
  return value.toLowerCase().replaceAll("_", " ").replace(/\b\w/g, (letter) => letter.toUpperCase());
}

function priceLabel(isFree?: boolean, price?: number, currency = "LKR") {
  if (isFree) return "Free";
  if (price == null) return "Pricing coming soon";
  return `${currency} ${Number(price).toLocaleString()}`;
}

function datePart(value: string | undefined) {
  return value?.slice(0, 10) ?? new Date().toISOString().slice(0, 10);
}

function timePart(value: string | undefined) {
  if (!value) return "TBA";
  return new Intl.DateTimeFormat("en", { hour: "numeric", minute: "2-digit" }).format(new Date(value));
}

function mapCourse(dto: CourseSummaryDto | CourseDetailDto): Course {
  const effectivePrice = dto.discountedPrice ?? dto.price;
  return {
    id: dto.id,
    slug: dto.slug,
    title: dto.title,
    category: dto.categoryName ?? "General",
    level: readableEnum(dto.level, "All levels"),
    duration: dto.durationHours ? `${dto.durationHours} hours` : "Self-paced",
    mode: "Online",
    priceLabel: priceLabel(dto.isFree, effectivePrice, dto.currency),
    summary: dto.description ?? dto.subtitle ?? "Course details are available from the HSSA catalogue.",
    outcomes: dto.tags ?? [],
    modules: [],
    featured: true,
    rating: 0,
    learners: 0,
  };
}

function mapResearch(dto: ResearchSummaryDto | ResearchDetailDto): ResearchItem {
  const detail = dto as ResearchDetailDto;
  const primaryAuthor = detail.members?.find((member) => member.isPrimary) ?? detail.members?.[0];
  return {
    id: dto.id,
    slug: dto.slug,
    title: dto.title,
    category: dto.categoryName ?? readableEnum(dto.researchType, "Research"),
    author: primaryAuthor ? `${primaryAuthor.firstName} ${primaryAuthor.lastName}` : "HSSA Research Desk",
    date: datePart(dto.publishedDate),
    summary: dto.abstractText ?? "Explore this research project from the HSSA repository.",
    abstract: dto.abstractText ?? detail.content ?? "No abstract has been published yet.",
    tags: detail.keywords ?? [readableEnum(dto.researchType, "Research")],
    downloads: [],
    featured: true,
  };
}

function mapWorkshop(dto: ProgramSummaryDto): Workshop {
  const past = ["COMPLETED", "CANCELLED"].includes(dto.status ?? "") || new Date(dto.startTime) < new Date();
  return {
    id: dto.id,
    title: dto.title,
    category: readableEnum(dto.eventType, "Workshop"),
    date: datePart(dto.startTime),
    time: timePart(dto.startTime),
    status: past ? "Past" : "Upcoming",
    format: readableEnum(dto.locationType, "Online"),
    facilitator: dto.organizerName ?? "HSSA Team",
    seats: dto.maxParticipants ?? 0,
    priceLabel: priceLabel(dto.isFree, dto.price),
    summary: dto.description ?? "View this workshop in the HSSA program calendar.",
  };
}

function mapEvent(dto: ProgramSummaryDto): EventItem {
  return {
    id: dto.id,
    title: dto.title,
    type: readableEnum(dto.eventType, "Event"),
    date: datePart(dto.startTime),
    time: timePart(dto.startTime),
    location: dto.locationDetails ?? readableEnum(dto.locationType, "Online"),
    summary: dto.description ?? "View this public program in the HSSA event calendar.",
    featured: true,
  };
}

export async function fetchCourses() {
  const response = await apiClient.post<ApiResponse<PageResponse<CourseSummaryDto>>>(
    "/courses/search?size=100&sort=publishedAt,desc",
    {},
  );
  return response.data.data.content.map(mapCourse);
}

export async function fetchCourseBySlug(slug: string) {
  const response = await apiClient.get<ApiResponse<CourseDetailDto>>(`/courses/slug/${encodeURIComponent(slug)}`);
  return mapCourse(response.data.data);
}

export async function fetchResearch() {
  const response = await apiClient.get<PageResponse<ResearchSummaryDto>>("/research/search", {
    params: { size: 100, sort: "publishedDate,desc" },
  });
  return response.data.content.map(mapResearch);
}

export async function fetchResearchBySlug(slug: string) {
  const response = await apiClient.get<ResearchDetailDto>(`/research/slug/${encodeURIComponent(slug)}`);
  return mapResearch(response.data);
}

export async function fetchWorkshops() {
  const response = await apiClient.get<PageResponse<ProgramSummaryDto>>("/workshops", {
    params: { size: 100, sort: "startTime,asc" },
  });
  return response.data.content.map(mapWorkshop);
}

export async function fetchEvents() {
  const response = await apiClient.get<PageResponse<ProgramSummaryDto>>("/events", {
    params: { size: 100, sort: "startTime,asc" },
  });
  return response.data.content.map(mapEvent);
}

export async function fetchFaqs() {
  const response = await apiClient.get<FaqDto[]>("/contact/faqs");
  return response.data
    .sort((left, right) => left.displayOrder - right.displayOrder)
    .map(({ question, answer }) => ({ question, answer }));
}
