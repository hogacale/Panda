export default function Instructions() {
  return (
    <div className="sidebar">
      <h1>TopBloc Coding Challenge</h1>
      You're tasked with creating a web application for Randy's Candies, a local
      candy store in Chicago. Randy, the owner, wants the application to do
      three things:
      <ol>
        <li>
          Display items that are almost out of stock (at less than 25% capacity)
        </li>
        <li>
          Allow him to input how many of each of those items he wants to
          re-order
        </li>
        <li>Determine the lowest total cost of re-ordering those items</li>
      </ol>
      The web application consists of a Java backend and React frontend.
      <h2>Backend</h2>
      <h3>Workbooks</h3>
      The backend contains two workbooks, <code>Inventory.xlsx</code> and{" "}
      <code>Distributors.xlsx</code>.<code>Inventory.xlsx</code> contains the
      items in the store: their SKU, name, amount in stock, and the capacity in
      the store for that item. <code>Distributors.xlsx</code> contains three
      worksheets, one for each distributor, which list the items the distributor
      sells: their SKU, item name, and price. Note that each distributor will
      not necessarily have all the items that Randy sells at his store.{" "}
      <p>
        It is up to you to find a Java library for reading workbooks, add it as
        a Maven dependency, and use it to read in the provided workbooks.
      </p>
      <h3>Endpoints</h3>
      The backend exposes two endpoints at <code>
        http://localhost:4567
      </code>{" "}
      using the <a href="https://sparkjava.com/">Java Spark</a> library:{" "}
      <code>/low-stock</code> and <code>/restock-cost</code>. The{" "}
      <code>/low-stock</code> endpoint should return only the items that are at
      less than 25% capacity. The <code>/restock-cost</code> endpoint should
      return the lowest total cost for Randy to restock. The lowest total cost
      is determined by the lowest available price across different distributors.
      The items to restock and their amounts should be provided in the request
      body from the frontend.
      <h2>Frontend</h2>
      <p>
        The frontend will be responsible for utilizing the Java endpoints to
        display the low-stock items and calculating the cost of restocking
        items. You will need to add the appropriate event listeners to the
        buttons for using these endpoints.
      </p>
      <p>
        You will also need to create a component to display a table row showing
        the item data. This component should also have an input element where
        Randy can type in the amount he wants to restock.
      </p>
    </div>
  );
}
