import {
  Link,
  createFileRoute,
  redirect,
  useNavigate,
} from "@tanstack/react-router"
import useUsers from "../../hooks/useUsers"
import { z } from "zod"
import { useEffect, useState } from "react"
import { formatPages } from "../../utils/utils"

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
      if (!users.data) return

      const newPerPage = input < 0 ? 1 : input > 100 ? 100 : input
      setInput(newPerPage)
      navigate({
        search: {
          page: users.data.totalUsersNumber / newPerPage < page ? 1 : page,
          usersPerPage: newPerPage,
        },
        replace: true,
      })
    }, 1000)

    return () => clearTimeout(handler)
  }, [input])

  const pages = formatPages({
    currentPage: page,
    totalPages: users.data?.totalPageNumber,
  })

  return (
    <div className="space-y-4">
      <div className="flex justify-end items-center gap-1">
        <p>Users per page:</p>
        <input
          type="number"
          min={1}
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
          {pages.map(({ label, value, key }) => {
            return (
              <Link
                key={key}
                search={{ page: value, usersPerPage }}
                className={
                  "join-item btn" +
                  (value === undefined ? " btn-disabled" : "") +
                  (value === page ? " btn-primary" : "")
                }
                disabled={value === page}
              >
                {label}
              </Link>
            )
          })}
        </div>
      </div>
    </div>
  )
}
