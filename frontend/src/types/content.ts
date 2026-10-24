export interface AboutContent {
  intro: string;
  vision: string;
  mission: string;
  quote: string;
  founder: {
    name: string;
    role: string;
    summary: string;
    interests: string[];
  };
  objectives: string[];
  timeline: TimelineItem[];
}

export interface TimelineItem {
  year: string;
  title: string;
  description: string;
}

export interface Course {
  id: string;
  slug: string;
  title: string;
  category: string;
  level: string;
  duration: string;
  mode: string;
  priceLabel: string;
  imageUrl?: string;
  summary: string;
  outcomes: string[];
  modules: string[];
  featured: boolean;
  rating: number;
  learners: number;
}

export interface PublicMetrics {
  totalCourses: number;
  totalStudents: number;
  totalInstructors: number;
  totalEvents: number;
}

export interface ResearchItem {
  id: string;
  slug: string;
  title: string;
  category: string;
  author: string;
  date: string;
  summary: string;
  abstract: string;
  tags: string[];
  downloads: string[];
  featured: boolean;
}

export interface Workshop {
  id: string;
  title: string;
  category: string;
  date: string;
  time: string;
  status: "Upcoming" | "Past";
  format: string;
  facilitator: string;
  seats: number;
  priceLabel: string;
  summary: string;
  imageUrl?: string;
}

export interface EventItem {
  id: string;
  title: string;
  type: string;
  date: string;
  time: string;
  location: string;
  summary: string;
  featured: boolean;
  imageUrl?: string;
}

export interface PublicationItem {
  id: string;
  slug: string;
  title: string;
  summary: string;
  content?: string;
  category: string;
  type: string;
  status: string;
  date: string;
  doi?: string;
  keywords: string[];
  authors: string[];
}

export interface BookItem {
  id: string;
  title: string;
  author: string;
  category: string;
  isbn?: string;
  featured: boolean;
}

export interface ContactOffice {
  id: string;
  officeName: string;
  address?: string;
  phone?: string;
  email?: string;
  openingHours?: string;
  googleMapUrl?: string;
  isHeadOffice: boolean;
}

export interface NewsItem {
  id: string;
  slug: string;
  title: string;
  category: string;
  date: string;
  readTime: string;
  summary: string;
  body: string;
  featured: boolean;
}

export interface SearchTopic {
  label: string;
  path: string;
  type: string;
}

export interface FaqItem {
  question: string;
  answer: string;
}
