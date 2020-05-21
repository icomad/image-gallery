document.addEventListener("DOMContentLoaded", () => {
    enableDeleteAlert();
});

function enableDeleteAlert(){
    const deleteAlertButtons = document.querySelectorAll('.alert-delete') || [];

    deleteAlertButtons.forEach(button => button.addEventListener('click', () => {
            const nodeToDelete = button.parentElement.parentElement.parentElement;
            nodeToDelete.parentElement.removeChild(nodeToDelete);
        })
    );
}