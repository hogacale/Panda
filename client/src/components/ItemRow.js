export default function ItemRow(props) {
    const rows = [];
    // console.log(itemList.items);
        if(props.itemRow !== null) {
            props.itemRow.forEach((items) => {
                rows.push(
                    <tr key={items.ID}>
                        <td>{items.ID}</td>
                        <td>{items.name}</td>
                        <td>{items.Stock}</td>
                        <td>{items.Capacity}</td>
                        <td><input
                            type={"search"}
                            onChange={props.handleInputChange}
                            id={items.ID}
                            />
                        </td>
                    </tr>
                );
            });
        }


        return (
            <>
                {rows}
            </>
        );
}