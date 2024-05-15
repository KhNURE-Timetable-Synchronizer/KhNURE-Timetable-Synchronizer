import { createFileRoute } from "@tanstack/react-router"
import useUsers from "../../hooks/useUsers"

export const Route = createFileRoute("/_auth/users")({
  component: Users,
})

function Users() {
  const { users } = useUsers()

  return (
    <div>
      {users.data?.users.map(u => {
        return (
          <div key={u.id}>
            {u.id} {u.role} {u.email}
          </div>
        )
      })}
    </div>
  )
}
