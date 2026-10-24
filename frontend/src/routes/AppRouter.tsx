import { lazy, Suspense, type ComponentType, type LazyExoticComponent } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import PageLoader from "@/components/common/PageLoader";
import ProtectedRoute from "@/components/auth/ProtectedRoute";
import MainLayout from "@/layouts/MainLayout";

const AboutPage = lazy(() => import("@/pages/About/AboutPage"));
const AccountPage = lazy(() => import("@/pages/Account/AccountPage"));
const ContactPage = lazy(() => import("@/pages/Contact/ContactPage"));
const CourseDetailsPage = lazy(() => import("@/pages/CourseDetails/CourseDetailsPage"));
const CoursesPage = lazy(() => import("@/pages/Courses/CoursesPage"));
const EventsPage = lazy(() => import("@/pages/Events/EventsPage"));
const ForgotPasswordPage = lazy(() => import("@/pages/ForgotPassword/ForgotPasswordPage"));
const HomePage = lazy(() => import("@/pages/Home/HomePage"));
const LoginPage = lazy(() => import("@/pages/Login/LoginPage"));
const AdminDashboardPage = lazy(() => import("@/pages/Management/AdminDashboardPage"));
const InstructorDashboardPage = lazy(() => import("@/pages/Management/InstructorDashboardPage"));
const NotFoundPage = lazy(() => import("@/pages/NotFound/NotFoundPage"));
const PublicationsPage = lazy(() => import("@/pages/Publications/PublicationsPage"));
const PublicationDetailsPage = lazy(() => import("@/pages/Publications/PublicationDetailsPage"));
const ReadingClubPage = lazy(() => import("@/pages/ReadingClub/ReadingClubPage"));
const RegisterPage = lazy(() => import("@/pages/Register/RegisterPage"));
const ResetPasswordPage = lazy(() => import("@/pages/ResetPassword/ResetPasswordPage"));
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
      { path: "reading-club", element: routeElement(ReadingClubPage) },
      { path: "workshops", element: routeElement(WorkshopsPage) },
      { path: "events", element: routeElement(EventsPage) },
      { path: "publications", element: routeElement(PublicationsPage) },
      { path: "publications/:id", element: routeElement(PublicationDetailsPage) },
      { path: "contact", element: routeElement(ContactPage) },
      { path: "search", element: routeElement(SearchPage) },
      { path: "login", element: routeElement(LoginPage) },
      { path: "forgot-password", element: routeElement(ForgotPasswordPage) },
      { path: "reset-password", element: routeElement(ResetPasswordPage) },
      { path: "register", element: routeElement(RegisterPage) },
      { path: "account", element: <ProtectedRoute>{routeElement(AccountPage)}</ProtectedRoute> },
      { path: "admin", element: <ProtectedRoute roles={["ADMIN"]}>{routeElement(AdminDashboardPage)}</ProtectedRoute> },
      { path: "instructor", element: <ProtectedRoute roles={["ADMIN", "INSTRUCTOR"]}>{routeElement(InstructorDashboardPage)}</ProtectedRoute> },
      { path: "*", element: routeElement(NotFoundPage) },
    ],
  },
]);

export default function AppRouter() {
  return <RouterProvider router={router} />;
}
