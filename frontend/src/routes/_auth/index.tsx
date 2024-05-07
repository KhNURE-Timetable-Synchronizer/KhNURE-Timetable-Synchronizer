import { Link, createFileRoute } from "@tanstack/react-router"
import useTimetables from "../../hooks/useTimetables"
import AddTimetableButton from "../../components/AddTimetableButton"
import DeleteTimetableButton from "../../components/DeleteTimetableButton"
import { z } from "zod"

const rootParamsSchema = z.object({
  id: z.number().optional(),
})

export const Route = createFileRoute("/_auth/")({
  component: Home,
  validateSearch: rootParamsSchema,
})

const d = [
  {
    id: 1,
    name: "knt",
    googleId:
      "c_a5db7533367412f08e9db638faed8f6d0062d06ca65bcb174c9fa68fe6ce6597@group.calendar.google.com",
  },
  {
    id: 2,
    name: "knt",
    googleId: "ru.ukrainian#holiday@group.v.calendar.google.com",
  },
]

function Home() {
  // const { personalTimetables } = useTimetables()
  const personalTimetables = { isLoading: false, data: d }
  const { id } = Route.useSearch()

  const calendarId = id
    ? personalTimetables.data?.find(timetable => timetable.id === id)?.googleId
    : personalTimetables.data?.[0]?.googleId

  return (
    <div className="flex gap-2 flex-col lg:flex-row">
      <div className="space-y-4 w-64">
        {personalTimetables.isLoading ? (
          <div className="flex justify-center">
            <span className="loading loading-spinner loading-md" />
          </div>
        ) : (
          <table className="table table-xs">
            <tbody>
              {personalTimetables.data?.map((timetable, i) => (
                <tr key={timetable.id} className="hover">
                  <td>
                    <Link
                      search={{ id: i == 0 ? undefined : timetable.id }}
                      className="btn btn-link"
                    >
                      {timetable.name}
                    </Link>
                  </td>
                  <td className="w-6">
                    <DeleteTimetableButton timetable={timetable} />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
        <AddTimetableButton />
      </div>
      {calendarId && (
        <iframe
          src={`https://calendar.google.com/calendar/embed?src=${encodeURIComponent(calendarId)}&ctz=Europe%2FKiev`}
          height="600"
          className="lg:flex-1 w-full"
        />
      )}
    </div>
  )
}
