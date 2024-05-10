import { StrictMode } from "react"
import ReactDOM from "react-dom/client"
import { RouterProvider, createRouter } from "@tanstack/react-router"

// Import the generated route tree
import { routeTree } from "./routeTree.gen"

import "./index.css"
import { AuthProvider, useAuth } from "./utils/AuthProvider"
import { GoogleOAuthProvider } from "@react-oauth/google"
import { QueryClient, QueryClientProvider } from "@tanstack/react-query"

const queryClient = new QueryClient()

// Create a new router instance
const router = createRouter({
  routeTree,
  context: {
    queryClient,
    auth: undefined!,
  },
})

// Register the router instance for type safety
declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router
  }
}

const Providers = ({ children }: { children: React.ReactNode }) => {
  return (
    <GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}>
      <AuthProvider>
        <QueryClientProvider client={queryClient}>
          {children}
        </QueryClientProvider>
      </AuthProvider>
    </GoogleOAuthProvider>
  )
}

const InnerRouter = () => {
  const auth = useAuth()

  return <RouterProvider router={router} context={{ auth, queryClient }} />
}

// Render the app
const rootElement = document.getElementById("root")!
if (!rootElement.innerHTML) {
  const root = ReactDOM.createRoot(rootElement)
  root.render(
    <StrictMode>
      <Providers>
        <InnerRouter />
      </Providers>
    </StrictMode>
  )
}
