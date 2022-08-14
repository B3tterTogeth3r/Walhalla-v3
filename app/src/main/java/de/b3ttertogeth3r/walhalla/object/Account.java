/*
 * Copyright (c) 2022-2022.
 *
 * Licensed under the Apace License, Version 2.0 (the "Licence"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 *  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *  either express or implied. See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.b3ttertogeth3r.walhalla.object;

public class Account {
    private int movements;
    private float amount = 0f;
    private float income = 0f;
    private float expense = 0f;

    public Account (float amount, int movements, float income, float expense) {
        this.amount = amount;
        this.movements = movements;
        this.income = income;
        this.expense = expense;
    }

    public Account () {
    }

    public float getAmount () {
        return amount;
    }

    public int getMovements () {
        return movements;
    }

    public float getIncome () {
        return income;
    }

    public float getExpense () {
        return expense;
    }
}
