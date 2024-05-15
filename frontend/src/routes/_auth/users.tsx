import {
  Link,
  createFileRoute,
  redirect,
  useNavigate,
} from "@tanstack/react-router"
import useUsers from "../../hooks/useUsers"
import { z } from "zod"
import { useEffect, useState } from "react"

const searchParamsSchema = z.object({
  page: z.number().optional(),
  usersPerPage: z.number().optional(),
})

export const Route = createFileRoute("/_auth/users")({
  beforeLoad: async ({ context }) => {
    if (context.auth.user?.role !== "ADMIN") {
      throw redirect({
        code: 401,
        to: "/",
        replace: true,
      })
    }
  },
  component: Users,
  validateSearch: searchParamsSchema,
})

function Users() {
  const searchParams = Route.useSearch()
  const page = searchParams.page || 1
  const usersPerPage = searchParams.usersPerPage || 10

  const { users } = useUsers({ page, usersPerPage })
  const navigate = useNavigate()

  const [input, setInput] = useState(usersPerPage)

  // debounced effect, will trigger only after 1 second of inactivity
  useEffect(() => {
    const handler = setTimeout(() => {
      navigate({ search: { page, usersPerPage: input }, replace: true })
    }, 1000)

    return () => clearTimeout(handler)
  }, [input])

  return (
    <div className="space-y-4">
      <div className="flex justify-end items-center gap-1">
        <p>Users per page:</p>
        <input
          type="number"
          min={2}
          max={100}
          value={input}
          className="input input-bordered input-sm"
          onChange={e => setInput(Number(e.target.value) || usersPerPage)}
        />
      </div>
      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Role</th>
          </tr>
        </thead>
        <tbody>
          {users.data?.users.map(u => (
            <tr key={u.id} className="hover">
              <th>{u.id}</th>
              <td>{u.email}</td>
              <td>{u.role}</td>
            </tr>
          ))}
        </tbody>
      </table>
      <div className="flex justify-center">
        <div className="join">
          {Array.from({ length: users.data?.totalPageNumber || 0 }).map(
            (_, i) => {
              return (
                <Link
                  key={i}
                  search={{ page: i + 1, usersPerPage }}
                  className={
                    "join-item btn" + (page === i + 1 ? " btn-active" : "")
                  }
                >
                  {i + 1}
                </Link>
              )
            }
          )}
        </div>
      </div>
    </div>
  )
}
