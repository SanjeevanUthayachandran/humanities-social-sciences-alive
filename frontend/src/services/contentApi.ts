import apiClient from "@/services/apiClient";
import type { ApiResponse, PageResponse } from "@/types/api";
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
  cardImageUrl?: string;
}

interface CourseDetailDto extends CourseSummaryDto {
  language?: string;
}

interface ModuleDto {
  id: string;
  title: string;
  displayOrder: number;
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
  coverMediaId?: string;
}

interface FaqDto extends FaqItem {
  id: string;
  displayOrder: number;
}

interface PublicationSummaryDto {
  id: string;
  title: string;
  slug: string;
  abstractText?: string;
  categoryName?: string;
  status?: string;
  publicationType?: string;
  publishedDate?: string;
}

interface PublicationDetailDto extends PublicationSummaryDto {
  content?: string;
  doi?: string;
  keywords?: string[];
  authors?: Array<{ firstName: string; lastName: string; isPrimary: boolean }>;
}

interface BookSummaryDto {
  id: string;
  title: string;
  author: string;
  isbn?: string;
  categoryName?: string;
  isFeatured?: boolean;
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
  return value?.slice(0, 10) ?? "";
}

function timePart(value: string | undefined) {
  if (!value) return "TBA";
  return new Intl.DateTimeFormat("en", { hour: "numeric", minute: "2-digit" }).format(new Date(value));
}

function mapCourse(dto: CourseSummaryDto | CourseDetailDto, modules: ModuleDto[] = []): Course {
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
    imageUrl: dto.cardImageUrl,
    summary: dto.description ?? dto.subtitle ?? "Course details are available from the HSSA catalogue.",
    outcomes: dto.tags ?? [],
    modules: [...modules].sort((left, right) => left.displayOrder - right.displayOrder).map((module) => module.title),
    featured: false,
    rating: 0,
    learners: 0,
  };
}

function mapPublication(dto: PublicationSummaryDto | PublicationDetailDto): PublicationItem {
  const detail = dto as PublicationDetailDto;
  return {
    id: dto.id,
    slug: dto.slug,
    title: dto.title,
    summary: dto.abstractText ?? "Publication details are available from the HSSA repository.",
    content: detail.content,
    category: dto.categoryName ?? "Publication",
    type: readableEnum(dto.publicationType, "Publication"),
    status: readableEnum(dto.status, "Published"),
    date: datePart(dto.publishedDate),
    doi: detail.doi,
    keywords: detail.keywords ?? [],
    authors: detail.authors?.map((author) => `${author.firstName} ${author.lastName}`) ?? [],
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
    featured: false,
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
    imageUrl: dto.coverMediaId ? `${String(apiClient.defaults.baseURL ?? "").replace(/\/$/, "")}/media/download/${dto.coverMediaId}` : undefined,
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
    featured: false,
    imageUrl: dto.coverMediaId ? `${String(apiClient.defaults.baseURL ?? "").replace(/\/$/, "")}/media/download/${dto.coverMediaId}` : undefined,
  };
}

export async function fetchCourses() {
  const response = await apiClient.post<ApiResponse<PageResponse<CourseSummaryDto>>>(
    "/courses/search?size=100&sort=publishedAt,desc",
    {},
  );
  return response.data.data.content.map((course) => mapCourse(course));
}

export async function fetchCourseBySlug(slug: string) {
  const response = await apiClient.get<ApiResponse<CourseDetailDto>>(`/courses/slug/${encodeURIComponent(slug)}`);
  const course = response.data.data;
  try {
    const moduleResponse = await apiClient.get<ApiResponse<ModuleDto[]>>(`/courses/${course.id}/modules`);
    return mapCourse(course, moduleResponse.data.data);
  } catch {
    return mapCourse(course);
  }
}

export async function fetchResearch() {
  const response = await apiClient.get<PageResponse<ResearchSummaryDto>>("/research/search", {
    params: { status: "PUBLISHED", size: 100, sort: "publishedDate,desc" },
  });
  return response.data.content.map(mapResearch);
}

export async function fetchPublicMetrics() {
  const response = await apiClient.get<PublicMetrics>("/dashboard/public");
  return response.data;
}

export async function fetchPublications() {
  const response = await apiClient.get<PageResponse<PublicationSummaryDto>>("/publications/search", {
    params: { status: "PUBLISHED", size: 100, sort: "publishedDate,desc" },
  });
  return response.data.content.map(mapPublication);
}

export async function fetchPublicationById(id: string) {
  const response = await apiClient.get<PublicationDetailDto>(`/publications/${encodeURIComponent(id)}`);
  return mapPublication(response.data);
}

export async function fetchBooks() {
  const response = await apiClient.get<PageResponse<BookSummaryDto>>("/books", {
    params: { size: 100, sort: "title,asc" },
  });
  return response.data.content.map<BookItem>((book) => ({
    id: book.id,
    title: book.title,
    author: book.author,
    category: book.categoryName ?? "Reading Club",
    isbn: book.isbn,
    featured: Boolean(book.isFeatured),
  }));
}

export async function fetchContactOffices() {
  const response = await apiClient.get<ContactOffice[]>("/contact/offices");
  return response.data;
}

export async function enrollInCourse(courseId: string) {
  await apiClient.post("/enrollments", { courseId });
}

export async function registerForWorkshop(workshopId: string) {
  await apiClient.post(`/workshops/${encodeURIComponent(workshopId)}/register`);
}

export async function registerForEvent(eventId: string) {
  await apiClient.post(`/events/${encodeURIComponent(eventId)}/register`);
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
