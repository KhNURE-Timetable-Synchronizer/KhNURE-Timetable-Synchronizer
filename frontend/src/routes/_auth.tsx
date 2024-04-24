import { createFileRoute, redirect } from "@tanstack/react-router"

export const Route = createFileRoute("/_auth")({
  beforeLoad: async ({ location, context }) => {
    if (!context.isAuthenticated) {
      throw redirect({
        code: 401,
        to: "/login",
        replace: true,
        search: {
          redirect: location.href,
        },
      })
    }
  },
})
