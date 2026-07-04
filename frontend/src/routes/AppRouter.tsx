import { lazy, Suspense, type ComponentType, type LazyExoticComponent } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import PageLoader from "@/components/common/PageLoader";
import MainLayout from "@/layouts/MainLayout";

const AboutPage = lazy(() => import("@/pages/About/AboutPage"));
const ContactPage = lazy(() => import("@/pages/Contact/ContactPage"));
const CourseDetailsPage = lazy(() => import("@/pages/CourseDetails/CourseDetailsPage"));
const CoursesPage = lazy(() => import("@/pages/Courses/CoursesPage"));
const EventsPage = lazy(() => import("@/pages/Events/EventsPage"));
const HomePage = lazy(() => import("@/pages/Home/HomePage"));
const LoginPage = lazy(() => import("@/pages/Login/LoginPage"));
const NewsPage = lazy(() => import("@/pages/News/NewsPage"));
const NotFoundPage = lazy(() => import("@/pages/NotFound/NotFoundPage"));
const RegisterPage = lazy(() => import("@/pages/Register/RegisterPage"));
const ResearchDetailsPage = lazy(() => import("@/pages/ResearchDetails/ResearchDetailsPage"));
const ResearchPage = lazy(() => import("@/pages/Research/ResearchPage"));
const SearchPage = lazy(() => import("@/pages/Search/SearchPage"));
const WorkshopsPage = lazy(() => import("@/pages/Workshops/WorkshopsPage"));

function routeElement(Page: LazyExoticComponent<ComponentType>) {
  return (
    <Suspense fallback={<PageLoader />}>
      <Page />
    </Suspense>
  );
}

const router = createBrowserRouter([
  {
    path: "/",
    element: <MainLayout />,
    children: [
      { index: true, element: routeElement(HomePage) },
      { path: "about", element: routeElement(AboutPage) },
      { path: "courses", element: routeElement(CoursesPage) },
      { path: "courses/:slug", element: routeElement(CourseDetailsPage) },
      { path: "research", element: routeElement(ResearchPage) },
      { path: "research/:slug", element: routeElement(ResearchDetailsPage) },
      { path: "workshops", element: routeElement(WorkshopsPage) },
      { path: "events", element: routeElement(EventsPage) },
      { path: "news", element: routeElement(NewsPage) },
      { path: "contact", element: routeElement(ContactPage) },
      { path: "search", element: routeElement(SearchPage) },
      { path: "login", element: routeElement(LoginPage) },
      { path: "register", element: routeElement(RegisterPage) },
      { path: "*", element: routeElement(NotFoundPage) },
    ],
  },
]);

export default function AppRouter() {
  return <RouterProvider router={router} />;
}
