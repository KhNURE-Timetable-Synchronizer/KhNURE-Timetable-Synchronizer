import { useState } from "react"
import useTimetables, { PersonalTimetable } from "../hooks/useTimetables"
import { Trash2Icon } from "lucide-react"

export default function DeleteTimetableButton({
  timetable,
}: {
  timetable: PersonalTimetable
}) {
  const { deleteTimetable, personalTimetables } = useTimetables()
  const [isLoading, setIsLoading] = useState(false)

  const onConfirm = async () => {
    setIsLoading(true)
    await deleteTimetable(timetable.id)
    setIsLoading(false)
    closeModal()
    await personalTimetables.refetch()
  }

  const openModal = () =>
    (
      document.getElementById(`delete-tt-modal-${timetable.id}`) as any
    ).showModal()
  const closeModal = () =>
    (document.getElementById(`delete-tt-modal-${timetable.id}`) as any).close()

  return (
    <>
      <button
        className="btn btn-xs hover:!text-white btn-outline btn-error btn-square"
        onClick={openModal}
      >
        <Trash2Icon size={14} />
      </button>

      <dialog id={`delete-tt-modal-${timetable.id}`} className="modal">
        <div className="modal-box">
          <h3 className="font-bold text-lg">Delete Timetable</h3>
          <p className="py-4">
            Are you sure you want to delete <strong>{timetable.name}</strong>{" "}
            timetable? This will remove the {timetable.name} calendar from your
            Google Calendar. You can always add it back later.
          </p>

          <div className="modal-action">
            <form method="dialog">
              {/* if there is a button in form, it will close the modal */}
              <button className="btn">Close</button>
            </form>

            <button
              className="btn btn-error"
              disabled={isLoading}
              onClick={onConfirm}
            >
              {isLoading && <span className="loading loading-spinner"></span>}
              Delete Timetable
            </button>
          </div>
        </div>
      </dialog>
    </>
  )
}
