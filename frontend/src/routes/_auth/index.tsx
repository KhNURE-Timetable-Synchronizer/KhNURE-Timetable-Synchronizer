import { createFileRoute } from "@tanstack/react-router"
import useTimetables from "../../hooks/useTimetables"
import AddTimetableButton from "../../components/AddTimetableButton"
import DeleteTimetableButton from "../../components/DeleteTimetableButton"

export const Route = createFileRoute("/_auth/")({
  component: Home,
})

function Home() {
  const { personalTimetables } = useTimetables()

  return (
    <div className="flex gap-2 flex-col md:flex-row">
      <div className="space-y-4 w-64">
        {personalTimetables.isLoading ? (
          <div className="flex justify-center">
            <span className="loading loading-spinner loading-md" />
          </div>
        ) : (
          <table className="table table-sm">
            <tbody>
              {personalTimetables.data?.map(timetable => (
                <tr key={timetable.id} className="hover">
                  <td>{timetable.name}</td>
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
      <div className="md:flex-1 bg-orange-300 h-20" />
    </div>
  )
}
