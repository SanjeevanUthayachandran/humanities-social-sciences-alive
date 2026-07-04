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
  summary: string;
  outcomes: string[];
  modules: string[];
  featured: boolean;
  rating: number;
  learners: number;
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
