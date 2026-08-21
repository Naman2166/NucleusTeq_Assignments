# Pizza Delivery Airflow DAG

This project simulates an automated pizza order processing workflow for **DoughFlow Pizza Co.** using Apache Airflow.

## Task Flow

```text
         take_order
             ↓
         check_stock
        /          \
       ↓            ↓
  bake_pizza     cancel_order
       ↓            |
 check_quality      |
       ↓            |
   pack_box         |
       ↓            |
 start_delivery     |
       |            |
       ↓            ↓
        order_status
```

The DAG first creates a random pizza order and then checks whether the required ingredients are available. If the ingredients are available, the pizza is baked, checked for quality, packed, sent for delivery and shows order successful message. If an ingredient is unavailable, the order is cancelled and refunded and shows order cancelled message. This branching design keeps the successful and cancellation flows separate and makes the workflow easy to understand and monitor.

<br>

## XCom Usage

The `take_order` task passes the following data through Airflow XCom:

- `order_id`
- `pizza_type`
- `is_premium`

Downstream tasks use this information to process the same order without needing an external database. For example, `bake_pizza` uses `is_premium` to determine the baking time, while other tasks use `order_id` and `pizza_type` for logging.

<br>

## Skip / Branch Condition

The `check_stock` task uses a `BranchPythonOperator`. Currently, **paneer is considered out of stock**.

If the selected pizza contains paneer, the `bake_pizza` path is skipped and the order goes directly to `cancel_order`.

Otherwise, the normal baking and delivery workflow continues.

<br>

## Schedule

The DAG runs using:

```text
0 11-13,17-20 * * *
```

This schedules the pipeline hourly during **11 AM–1 PM and 5 PM–8 PM**, representing typical lunch and dinner ordering periods.

`catchup=False` is used so Airflow does not run old missed schedules when the DAG is started.
