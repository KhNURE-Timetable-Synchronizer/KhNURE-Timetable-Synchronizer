import {
  Link,
  createFileRoute,
  redirect,
  useNavigate,
} from "@tanstack/react-router"
import { z } from "zod"
import useRequestsAdmin from "../../../hooks/userRequestsAdmin"
import { useEffect, useState } from "react"
import { formatPages } from "../../../utils/utils"

const searchParamsSchema = z.object({
  page: z.number().optional(),
  requestsPerPage: z.number().optional(),
})

export const Route = createFileRoute("/_auth/requests/")({
  component: Requests,
  beforeLoad: async ({ context }) => {
    if (context.auth.user?.role !== "ADMIN") {
      throw redirect({
        code: 401,
        to: "/",
        replace: true,
      })
    }
  },
  validateSearch: searchParamsSchema,
})

function Requests() {
  const searchParams = Route.useSearch()
  const page = searchParams.page || 1
  const requestsPerPage = searchParams.requestsPerPage || 10

  const { requests } = useRequestsAdmin({ page, requestsPerPage })
  const navigate = useNavigate()

  const [input, setInput] = useState(requestsPerPage)

  // debounced effect, will trigger only after 1 second of inactivity
  useEffect(() => {
    const handler = setTimeout(() => {
      if (!requests.data) return

      const newPerPage = input < 0 ? 1 : input > 100 ? 100 : input
      setInput(newPerPage)
      navigate({
        search: {
          page:
            requests.data.totalRequestsNumber / newPerPage < page ? 1 : page,
          requestsPerPage: newPerPage,
        },
        replace: true,
      })
    }, 1000)

    return () => clearTimeout(handler)
  }, [input])

  const pages = formatPages({
    currentPage: page,
    totalPages: requests.data?.totalPageNumber,
  })

  return (
    <div className="space-y-4">
      <div className="flex justify-end items-center gap-1">
        <p>Requests per page:</p>
        <input
          type="number"
          min={1}
          max={100}
          value={input}
          className="input input-bordered input-sm"
          onChange={e => setInput(Number(e.target.value) || requestsPerPage)}
        />
      </div>
      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Requested Timetable</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {requests.data?.requests.map(r => (
            <tr key={r.id} className="hover">
              <th>
                <Link to={`/requests/${r.id}`} className="link">
                  {r.id}
                </Link>
              </th>
              <td>{r.email}</td>
              <td>{r.requestedTimetable.name}</td>
              <td>{r.status}</td>
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
                search={{ page: value, requestsPerPage: requestsPerPage }}
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
