import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cours.reducer';

export const CoursDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const coursEntity = useAppSelector(state => state.cours.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="coursDetailsHeading">
          <Translate contentKey="isimmManagerApp.cours.detail.title">Cours</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{coursEntity.id}</dd>
          <dt>
            <span id="titre">
              <Translate contentKey="isimmManagerApp.cours.titre">Titre</Translate>
            </span>
          </dt>
          <dd>{coursEntity.titre}</dd>
          <dt>
            <span id="file">
              <Translate contentKey="isimmManagerApp.cours.file">File</Translate>
            </span>
          </dt>
          <dd>
            {coursEntity.file ? (
              <div>
                {coursEntity.fileContentType ? (
                  <a onClick={openFile(coursEntity.fileContentType, coursEntity.file)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {coursEntity.fileContentType}, {byteSize(coursEntity.file)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="isimmManagerApp.cours.matiere">Matiere</Translate>
          </dt>
          <dd>{coursEntity.matiere ? coursEntity.matiere.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/cours" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cours/${coursEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CoursDetail;
