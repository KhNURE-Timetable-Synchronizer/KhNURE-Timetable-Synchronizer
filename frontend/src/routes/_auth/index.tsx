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

function Home() {
  const { personalTimetables: timetables } = useTimetables()
  const { id } = Route.useSearch()

  const calendarId = id
    ? timetables.data?.find(timetable => timetable.id === id)?.googleCalendarId
    : timetables.data?.[0]?.googleCalendarId

  return (
    <div className="flex gap-2 flex-col lg:flex-row">
      <div className="space-y-4 w-64">
        {timetables.isLoading ? (
          <div className="flex justify-center">
            <span className="loading loading-spinner loading-md" />
          </div>
        ) : (
          <table className="table table-xs">
            <tbody>
              {timetables.data?.map((timetable, i) => (
                <tr key={timetable.id} className="hover">
                  <td>
                    <Link
                      search={{ id: i == 0 ? undefined : timetable.id }}
                      className="btn btn-link btn-sm"
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
      {calendarId ? (
        <iframe
          src={`https://calendar.google.com/calendar/embed?src=${encodeURIComponent(calendarId)}&ctz=Europe%2FKiev`}
          height="600"
          className="lg:flex-1 w-full"
        />
      ) : (
        <p className="place-self-center ml-4">{"<"}- Select timetable</p>
      )}
    </div>
  )
}
