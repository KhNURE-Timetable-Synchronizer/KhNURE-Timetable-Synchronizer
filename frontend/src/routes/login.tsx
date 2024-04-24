import { createFileRoute, redirect } from "@tanstack/react-router"

export const Route = createFileRoute("/login")({
  beforeLoad: async ({ context }) => {
    if (context.isAuthenticated) {
      throw redirect({
        code: 403,
        to: "/",
      })
    }
  },
  component: Login,
})

function Login() {
  return <div className="p-2">Hello from Login!</div>
}
