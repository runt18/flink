/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.api.table.plan.rules

import org.apache.calcite.rel.rules._
import org.apache.calcite.tools.{RuleSets, RuleSet}
import org.apache.flink.api.table.plan.rules.dataSet._

object FlinkRuleSets {

  /**
    * RuleSet to optimize plans for batch / DataSet exeuction
    */
  val DATASET_OPT_RULES: RuleSet = RuleSets.ofList(

    // push a filter into a join
    FilterJoinRule.FILTER_ON_JOIN,
    // push filter into the children of a join
    FilterJoinRule.JOIN,
    // push filter through an aggregation
    FlinkFilterAggregateTransposeRule.INSTANCE,

    // aggregation and projection rules
    AggregateProjectMergeRule.INSTANCE,
    AggregateProjectPullUpConstantsRule.INSTANCE,
    // push a projection past a filter or vice versa
    ProjectFilterTransposeRule.INSTANCE,
    FilterProjectTransposeRule.INSTANCE,
    // push a projection to the children of a join
    ProjectJoinTransposeRule.INSTANCE,
    // remove identity project
    ProjectRemoveRule.INSTANCE,
    // reorder sort and projection
    SortProjectTransposeRule.INSTANCE,
    ProjectSortTransposeRule.INSTANCE,

    // join rules
    JoinPushExpressionsRule.INSTANCE,

    // remove union with only a single child
    UnionEliminatorRule.INSTANCE,
    // convert non-all union into all-union + distinct
    UnionToDistinctRule.INSTANCE,

    // remove aggregation if it does not aggregate and input is already distinct
    AggregateRemoveRule.INSTANCE,
    // push aggregate through join
    AggregateJoinTransposeRule.EXTENDED,
    // aggregate union rule
    AggregateUnionAggregateRule.INSTANCE,

    // remove unnecessary sort rule
    SortRemoveRule.INSTANCE,

    // simplify expressions rules
    ReduceExpressionsRule.CALC_INSTANCE,
    ReduceExpressionsRule.JOIN_INSTANCE,

    // prune empty results rules
    PruneEmptyRules.AGGREGATE_INSTANCE,
    PruneEmptyRules.FILTER_INSTANCE,
    PruneEmptyRules.JOIN_LEFT_INSTANCE,
    PruneEmptyRules.JOIN_RIGHT_INSTANCE,
    PruneEmptyRules.PROJECT_INSTANCE,
    PruneEmptyRules.SORT_INSTANCE,
    PruneEmptyRules.UNION_INSTANCE,

    // calc rules
    FilterCalcMergeRule.INSTANCE,
    ProjectCalcMergeRule.INSTANCE,
    FilterToCalcRule.INSTANCE,
    ProjectToCalcRule.INSTANCE,
    CalcMergeRule.INSTANCE,

    // translate to Flink DataSet nodes
    DataSetAggregateRule.INSTANCE,
    DataSetCalcRule.INSTANCE,
    DataSetJoinRule.INSTANCE,
    DataSetScanRule.INSTANCE,
    DataSetUnionRule.INSTANCE
  )

}
