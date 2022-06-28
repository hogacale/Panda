import {useState} from "react";
import ItemRow from "./ItemRow";


export default function Challenge() {
    const [lowStockItems, setLowStockItems] = useState(null);
    const [reOrderCost, setReOrderCost] = useState(null);
    const reOrderPrices = new Map();

    function getLowStock(){
        fetch('http://localhost:4567/low-stock')
            .then((res) => res.json())
            .then((res) => {
                setLowStockItems(res);
            });
    }

    function getReOrderAmount(e){
        if(e.target.value === ""){
            reOrderPrices.set(e.target.id, 0);
            return
        }
        reOrderPrices.set(e.target.id, e.target.value);
    }

    function getItemList(){
        let itemList = [];
        let itemObject;
        lowStockItems.forEach((item,index) => {
            if(reOrderPrices.get(item.ID) === undefined){
                itemObject = {id: item.ID, amount: "0"};
            } else {
                itemObject = {id: item.ID, amount: reOrderPrices.get(item.ID)};
            }
            itemList[index] = itemObject;
        });
        return itemList;
    }

    function getReOrderPrice() {
        let totalPrice = 0;
        const reOrderItemList = getItemList();

        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(reOrderItemList)

        };

        fetch('http://localhost:4567/restock-cost', requestOptions)
            .then((res) => { res.json()
            .then((data) => {
                data.forEach((price) => {
                    totalPrice += price.price;
                });
                setReOrderCost(totalPrice);
            })});
    }

  return (
    <>
      <table>
        <thead>
          <tr>
            <td>SKU</td>
            <td>Item Name</td>
            <td>Amount in Stock</td>
            <td>Capacity</td>
            <td>Order Amount</td>
          </tr>
        </thead>
        <tbody>
          {/* 
          TODO: Create an <ItemRow /> component that's rendered for every inventory item. The component
          will need an input element in the Order Amount column that will take in the order amount and 
          update the application state appropriately.
          */}
          <ItemRow
              itemRow={lowStockItems}
              handleInputChange={getReOrderAmount}
          />
        </tbody>
      </table>
      {/* TODO: Display total cost returned from the server */}
      <div>Total Cost: {reOrderCost}</div>
      {/* 
      TODO: Add event handlers to these buttons that use the Java API to perform their relative actions.
      */}
      <button onClick={getLowStock}>Get Low-Stock Items</button>
      <button onClick={getReOrderPrice}>Determine Re-Order Cost</button>
    </>
  );
}
